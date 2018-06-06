package ast.expressions;

import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import static org.bytedeco.javacpp.LLVM.LLVMBuildSDiv;
import cg.CodeGen;

public class DivExpr extends BinaryOpExpr {
    public DivExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public LLVMValueRef doBinaryCG(CodeGen codegen, LLVMValueRef left, LLVMValueRef right) {
        return LLVMBuildSDiv(codegen.getBuilder(), left, right, "");
    }

    @Override
    char getOperator() {
        return '/';
    }
}
