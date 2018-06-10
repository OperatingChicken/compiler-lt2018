package ast.expressions;

import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import static org.bytedeco.javacpp.LLVM.LLVMBuildSRem;
import cg.CodeGen;

public class ModExpr extends BinaryOpExpr {
    public ModExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public LLVMValueRef doBinaryCG(CodeGen codegen, LLVMValueRef left, LLVMValueRef right) {
        return LLVMBuildSRem(codegen.getBuilder(), left, right, "");
    }

    @Override
    char getOperator() {
        return '%';
    }
}
