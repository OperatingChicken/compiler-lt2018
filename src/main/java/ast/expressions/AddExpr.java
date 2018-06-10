package ast.expressions;

import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import static org.bytedeco.javacpp.LLVM.LLVMBuildAdd;
import cg.CodeGen;

public class AddExpr extends BinaryOpExpr {
    public AddExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public LLVMValueRef doBinaryCG(CodeGen codegen, LLVMValueRef left, LLVMValueRef right) {
        return LLVMBuildAdd(codegen.getBuilder(), left, right, "");
    }

    @Override
    char getOperator() {
        return '+';
    }
}
