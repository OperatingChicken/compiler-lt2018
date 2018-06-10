package ast.expressions;

import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import static org.bytedeco.javacpp.LLVM.LLVMBuildMul;
import cg.CodeGen;

public class MulExpr extends BinaryOpExpr {
    public MulExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public LLVMValueRef doBinaryCG(CodeGen codegen, LLVMValueRef left, LLVMValueRef right) {
        return LLVMBuildMul(codegen.getBuilder(), left, right, "");
    }

    @Override
    char getOperator() {
        return '*';
    }
}
