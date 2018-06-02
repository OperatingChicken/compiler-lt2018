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
        PointerPointer printf_function_params = new PointerPointer(new LLVMTypeRef[] { LLVMPointerType(LLVMInt8Type(), 0) });
        PointerPointer scanf_function_params = new PointerPointer(new LLVMTypeRef[] { LLVMPointerType(LLVMInt8Type(), 0) });
        LLVMTypeRef main_function_type = LLVMFunctionType(LLVMInt32Type(), main_function_params, 2, 0);
        LLVMTypeRef printf_function_type = LLVMFunctionType(LLVMInt32Type(), printf_function_params, 1, 1);
        LLVMTypeRef scanf_function_type = LLVMFunctionType(LLVMInt32Type(), scanf_function_params, 1, 1);
        this.main_function = LLVMAddFunction(this.module, "main", main_function_type);
        LLVMValueRef printf_function = LLVMAddFunction(this.module, "printf", printf_function_type);
        LLVMValueRef scanf_function = LLVMAddFunction(this.module, "scanf", scanf_function_type);
        LLVMSetFunctionCallConv(this.main_function, LLVMCCallConv);
        LLVMSetFunctionCallConv(printf_function, LLVMCCallConv);
        LLVMSetLinkage(printf_function, LLVMExternalLinkage);
        LLVMSetFunctionCallConv(scanf_function, LLVMCCallConv);
        LLVMSetLinkage(scanf_function, LLVMExternalLinkage);
        // TODO
        this.return_block = LLVMAppendBasicBlock(this.main_function, "");
        this.builder = LLVMCreateBuilder();
        LLVMPositionBuilderAtEnd(this.builder, this.return_block);
        LLVMValueRef printer_string = LLVMBuildGlobalStringPtr(this.builder, "Insert your lucky number: ", "");
        PointerPointer printf_params = new PointerPointer(new LLVMValueRef[] { printer_string } );
        LLVMBuildCall(this.builder, printf_function, printf_params, 1, "");
        LLVMValueRef to_read_string = LLVMBuildGlobalStringPtr(this.builder, "%d", "");
        LLVMValueRef variable = LLVMBuildAlloca(this.builder, LLVMInt32Type(), "");
        PointerPointer scanf_params = new PointerPointer(new LLVMValueRef[] { to_read_string, variable });
        LLVMBuildCall(this.builder, scanf_function, scanf_params, 2, "");
        LLVMValueRef hello_world_string = LLVMBuildGlobalStringPtr(this.builder, "Your lucky number: %d\n", "");
        printf_params = new PointerPointer(new LLVMValueRef[] { hello_world_string, LLVMBuildLoad(this.builder, variable, "") });
        LLVMBuildCall(this.builder, printf_function, printf_params, 2, "");
        LLVMBuildRet(this.builder, LLVMConstInt(LLVMInt32Type(), 0, 0));
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
