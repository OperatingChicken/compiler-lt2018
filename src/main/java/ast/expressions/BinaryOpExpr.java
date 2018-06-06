package ast.expressions;

import java.util.Set;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import cg.CodeGen;

public abstract class BinaryOpExpr extends Expr {
    private final Expr leftOperand;
    private final Expr rightOperand;
    public abstract LLVMValueRef doBinaryCG(CodeGen codegen, LLVMValueRef left, LLVMValueRef right);

    BinaryOpExpr(Expr leftOperand, Expr rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Set<String> getIdentifiers() {
        Set<String> result = this.leftOperand.getIdentifiers();
        result.addAll(this.rightOperand.getIdentifiers());
        return result;
    }

    public LLVMValueRef codeGen(CodeGen codegen) {
        LLVMValueRef leftValue = this.leftOperand.codeGen(codegen);
        LLVMValueRef rightValue = this.rightOperand.codeGen(codegen);
        return this.doBinaryCG(codegen, leftValue, rightValue);
    }

    abstract char getOperator();

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + " " + getOperator() + " " + rightOperand.toString() + ")";
    }
}
