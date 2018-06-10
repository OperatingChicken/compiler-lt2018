package ast.expressions;

import cg.CodeGen;
import org.bytedeco.javacpp.LLVM;

import java.util.HashSet;
import java.util.Set;
import org.bytedeco.javacpp.LLVM.LLVMBasicBlockRef;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import org.bytedeco.javacpp.PointerPointer;
import static org.bytedeco.javacpp.LLVM.LLVMBuildBr;
import static org.bytedeco.javacpp.LLVM.LLVMBuildCondBr;
import static org.bytedeco.javacpp.LLVM.LLVMPositionBuilderAtEnd;
import static org.bytedeco.javacpp.LLVM.LLVMAddIncoming;
import static org.bytedeco.javacpp.LLVM.LLVMInt64Type;
import static org.bytedeco.javacpp.LLVM.LLVMBuildPhi;

public class IfExpr extends Expr {
    private final Expr cond;
    private final Expr ifTrue;
    private final Expr ifFalse;

    public IfExpr(Expr cond, Expr ifTrue, Expr ifFalse) {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public LLVM.LLVMValueRef codeGen(CodeGen codegen) {
        codegen.popBlockStack();
        LLVMBasicBlockRef condBB = codegen.newBlock();
        LLVMBasicBlockRef ifTrueBB = codegen.newBlock();
        LLVMBasicBlockRef ifFalseBB = codegen.newBlock();
        LLVMBasicBlockRef afterBB = codegen.newBlock();
        LLVMBuildBr(codegen.getBuilder(), condBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), condBB);
        codegen.pushBlockStack(condBB);
        LLVMValueRef cond_value = codegen.getBoolean(this.cond.codeGen(codegen));
        codegen.popBlockStack();
        LLVMBuildCondBr(codegen.getBuilder(), cond_value, ifTrueBB, ifFalseBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), ifTrueBB);
        codegen.pushBlockStack(ifTrueBB);
        LLVMValueRef ifTrue_value = this.ifTrue.codeGen(codegen);
        LLVMBasicBlockRef newIfTrueBB = codegen.popBlockStack();
        LLVMBuildBr(codegen.getBuilder(), afterBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), ifFalseBB);
        codegen.pushBlockStack(ifFalseBB);
        LLVMValueRef ifFalse_value = this.ifFalse.codeGen(codegen);
        LLVMBasicBlockRef newIfFalseBB = codegen.popBlockStack();
        LLVMBuildBr(codegen.getBuilder(), afterBB);
        LLVMPositionBuilderAtEnd(codegen.getBuilder(), afterBB);
        codegen.pushBlockStack(afterBB);
        LLVMValueRef result = LLVMBuildPhi(codegen.getBuilder(), LLVMInt64Type(), "");
        LLVMValueRef[] phi_values = { ifTrue_value, ifFalse_value };
        LLVMBasicBlockRef[] phi_blocks = { newIfTrueBB, newIfFalseBB };
        LLVMAddIncoming(result, new PointerPointer(phi_values), new PointerPointer(phi_blocks), 2);
        return result;
    }

    @Override
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<>();
        result.addAll(cond.getIdentifiers());
        result.addAll(ifTrue.getIdentifiers());
        result.addAll(ifFalse.getIdentifiers());
        return result;
    }

    @Override
    public String toString() {
        return "( " + cond.toString() + " ? " + ifTrue.toString() + " : " + ifFalse.toString() + " )";
    }
}
