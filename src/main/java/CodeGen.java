import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.LLVM.*;

public class CodeGen {
    private static CodeGen singleton = null;
    private BytePointer error;
    private LLVMModuleRef module;
    private LLVMValueRef main_function;
    private LLVMBuilderRef builder;
    private LLVMBasicBlockRef return_block;

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
        PointerPointer exit_function_params = new PointerPointer(new LLVMTypeRef[] { LLVMInt32Type() });
        LLVMTypeRef main_function_type = LLVMFunctionType(LLVMInt32Type(), main_function_params, 2, 0);
        LLVMTypeRef exit_function_type = LLVMFunctionType(LLVMVoidType(), exit_function_params, 1, 0);
        this.main_function = LLVMAddFunction(this.module, "main", main_function_type);
        LLVMValueRef exit_function = LLVMAddFunction(this.module, "exit", exit_function_type);
        LLVMSetFunctionCallConv(this.main_function, LLVMCCallConv);
        LLVMSetFunctionCallConv(exit_function, LLVMCCallConv);
        LLVMSetLinkage(exit_function, LLVMExternalLinkage);
        // TODO
        this.return_block = LLVMAppendBasicBlock(this.main_function, "return");
        this.builder = LLVMCreateBuilder();
        LLVMPositionBuilderAtEnd(this.builder, this.return_block);
        LLVMValueRef return_value = LLVMConstInt(LLVMInt32Type(), 42, 0);
        //LLVMBuildCall(this.builder, exit_function, return_value, 1, new BytePointer(""));
        LLVMBuildRet(this.builder, return_value);
        LLVMVerifyModule(this.module, LLVMAbortProcessAction, error);
        LLVMDisposeMessage(error);
        BytePointer target_triple = LLVMGetDefaultTargetTriple();
        LLVMSetTarget(this.module, target_triple);
        LLVMTargetRef target = new LLVMTargetRef();
        if(LLVMGetTargetFromTriple(target_triple, target, error) != 0) {
            System.err.println(error);
            System.exit(1);
        }
        LLVMDisposeMessage(error);
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
        BytePointer output_file = new BytePointer("output.o");
        if(LLVMTargetMachineEmitToFile(target_machine, this.module, output_file, LLVMObjectFile, error) != 0) {
            System.err.println(error);
            System.exit(1);
        }
        LLVMDisposeMessage(error);
        // DEBUG
        String module_string = LLVMPrintModuleToString(this.module).getString();
        System.err.println(module_string);
    }

    public static synchronized CodeGen getSingleton() {
        if(CodeGen.singleton == null) {
            CodeGen.singleton = new CodeGen();
        }
        return CodeGen.singleton;
    }
}
