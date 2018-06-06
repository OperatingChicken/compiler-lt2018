package ast.expressions;

import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import static org.bytedeco.javacpp.LLVM.LLVMBuildSub;
import cg.CodeGen;

public class SubExpr extends BinaryOpExpr {
    public SubExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public LLVMValueRef doBinaryCG(CodeGen codegen, LLVMValueRef left, LLVMValueRef right) {
        return LLVMBuildSub(codegen.getBuilder(), left, right, "");
    }

    @Override
    char getOperator() {
        return '-';
    }
}
