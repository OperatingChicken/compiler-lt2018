package cg;

import java.util.Set;
import java.util.HashMap;
import java.util.Stack;
import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.LLVM.*;

public class CodeGen {
    private static CodeGen singleton = null;
    private BytePointer error;
    private LLVMModuleRef module;
    private LLVMBuilderRef builder;
    private LLVMValueRef main_function;
    private LLVMValueRef printf_function;
    private LLVMValueRef scanf_function;
    private HashMap<String, LLVMValueRef> variables;
    private LLVMValueRef variable_string;
    private LLVMValueRef tmp_variable;
    private LLVMValueRef zero_constant;
    private Stack<LLVMBasicBlockRef> blocks;

    private CodeGen() {
        this.error = new BytePointer((Pointer)null);
        LLVMInitializeAllTargetInfos();
        LLVMInitializeAllTargets();
        LLVMInitializeAllTargetMCs();
        LLVMInitializeAllAsmParsers();
        LLVMInitializeAllAsmPrinters();
        LLVMInitializeAllDisassemblers();
        this.module = LLVMModuleCreateWithName("main_module");
        PointerPointer main_function_params = new PointerPointer(new LLVMTypeRef[] { LLVMInt32Type(), LLVMPointerType(LLVMInt8Type(), 0) });
        PointerPointer printf_function_params = new PointerPointer(new LLVMTypeRef[] { LLVMPointerType(LLVMInt8Type(), 0) });
        PointerPointer scanf_function_params = new PointerPointer(new LLVMTypeRef[] { LLVMPointerType(LLVMInt8Type(), 0) });
        LLVMTypeRef main_function_type = LLVMFunctionType(LLVMInt32Type(), main_function_params, 2, 0);
        LLVMTypeRef printf_function_type = LLVMFunctionType(LLVMInt32Type(), printf_function_params, 1, 1);
        LLVMTypeRef scanf_function_type = LLVMFunctionType(LLVMInt32Type(), scanf_function_params, 1, 1);
        this.main_function = LLVMAddFunction(this.module, "main", main_function_type);
        this.printf_function = LLVMAddFunction(this.module, "printf", printf_function_type);
        this.scanf_function = LLVMAddFunction(this.module, "scanf", scanf_function_type);
        LLVMSetFunctionCallConv(this.main_function, LLVMCCallConv);
        LLVMSetFunctionCallConv(printf_function, LLVMCCallConv);
        LLVMSetLinkage(printf_function, LLVMExternalLinkage);
        LLVMSetFunctionCallConv(scanf_function, LLVMCCallConv);
        LLVMSetLinkage(scanf_function, LLVMExternalLinkage);
        LLVMBasicBlockRef main_block = LLVMAppendBasicBlock(this.main_function, "");
        this.builder = LLVMCreateBuilder();
        LLVMPositionBuilderAtEnd(this.builder, main_block);
        this.variable_string = LLVMBuildGlobalStringPtr(this.builder, "%lld", "");
        this.tmp_variable = LLVMBuildAlloca(this.builder, LLVMInt64Type(), "");
        this.zero_constant = LLVMConstInt(LLVMInt64Type(), 0, 0);
        this.blocks = new Stack<>();
        this.blocks.push(main_block);
    }

    public void initVariables(Set<String> variables) {
        this.variables = new HashMap<>();
        for(String name: variables) {
            this.variables.put(name, LLVMBuildAlloca(this.builder, LLVMInt64Type(), ""));
        }
    }

    public LLVMBuilderRef getBuilder() {
        return this.builder;
    }

    public LLVMValueRef getVariable(String name) {
        return LLVMBuildLoad(this.builder, this.variables.get(name), "");
    }

    public void setVariable(String name, LLVMValueRef value) {
        LLVMBuildStore(this.builder, value, this.variables.get(name));
    }

    public LLVMValueRef inputValue() {
        PointerPointer scanf_params = new PointerPointer(new LLVMValueRef[] { this.variable_string, this.tmp_variable });
        LLVMBuildCall(this.builder, this.scanf_function, scanf_params, 2, "");
        return LLVMBuildLoad(this.builder, this.tmp_variable, "");
    }

    public void outputValue(LLVMValueRef value) {
        PointerPointer printf_params = new PointerPointer(new LLVMValueRef[] { this.variable_string, value });
        LLVMBuildCall(this.builder, this.printf_function, printf_params, 2, "");
    }

    public void outputString(String s) {
        PointerPointer printf_params = new PointerPointer(new LLVMValueRef[] { LLVMBuildGlobalStringPtr(this.builder, s, "") });
        LLVMBuildCall(this.builder, this.printf_function, printf_params, 1, "");
    }

    public LLVMValueRef getConstant(long value) {
        return LLVMConstInt(LLVMInt64Type(), value, 0);
    }

    public void pushBlockStack(LLVMBasicBlockRef block) {
        this.blocks.push(block);
    }

    public LLVMBasicBlockRef popBlockStack() {
        return this.blocks.pop();
    }

    public LLVMBasicBlockRef peekBlockStack() {
        return this.blocks.peek();
    }

    public LLVMBasicBlockRef newBlock() {
        return LLVMAppendBasicBlock(this.main_function, "");
    }

    public LLVMValueRef getBoolean(LLVMValueRef value) {
        return LLVMBuildICmp(this.builder, LLVMIntNE, value, this.zero_constant, "");
    }

    public void emitObj(String path, boolean printIrToStdout, String printIrToFile) {
        this.blocks.pop();
        if(!this.blocks.empty()) {
            System.err.println("Internal stack error!");
            System.exit(1);
        }
        LLVMBuildRet(this.builder, LLVMConstInt(LLVMInt32Type(), 0, 0));
        LLVMVerifyModule(this.module, LLVMAbortProcessAction, error);
        BytePointer target_triple = LLVMGetDefaultTargetTriple();
        LLVMSetTarget(this.module, target_triple);
        LLVMTargetRef target = new LLVMTargetRef();
        if(LLVMGetTargetFromTriple(target_triple, target, error) != 0) {
            System.err.println(error);
            System.exit(1);
        }
        LLVMTargetMachineRef target_machine = LLVMCreateTargetMachine(target, target_triple.getString(), "generic", "", LLVMCodeGenLevelDefault, LLVMRelocPIC, LLVMCodeModelDefault);
        LLVMTargetDataRef data_layout = LLVMCreateTargetDataLayout(target_machine);
        BytePointer data_layout_string = LLVMCopyStringRepOfTargetData(data_layout);
        LLVMSetDataLayout(this.module, data_layout_string);
        LLVMPassManagerRef pass = LLVMCreatePassManager();
        LLVMAddConstantPropagationPass(pass);
        LLVMAddInstructionCombiningPass(pass);
        LLVMAddPromoteMemoryToRegisterPass(pass);
        LLVMAddGVNPass(pass);
        LLVMAddCFGSimplificationPass(pass);
        LLVMRunPassManager(pass, this.module);
        BytePointer output_file = new BytePointer(path);
        if(LLVMTargetMachineEmitToFile(target_machine, this.module, output_file, LLVMObjectFile, error) != 0) {
            System.err.println(error);
            System.exit(1);
        }
        if(printIrToStdout) {
            System.err.println("LLVM-IR dump:");
            String module_string = LLVMPrintModuleToString(this.module).getString();
            System.err.println(module_string);
        }
        if(printIrToFile != null)  {
            LLVMPrintModuleToFile(this.module, printIrToFile, error);
        }
    }

    public static synchronized CodeGen getInstance() {
        if(CodeGen.singleton == null) {
            CodeGen.singleton = new CodeGen();
        }
        return CodeGen.singleton;
    }
}
