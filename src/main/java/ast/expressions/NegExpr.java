package ast.expressions;

import cg.CodeGen;
import org.bytedeco.javacpp.LLVM;
import static org.bytedeco.javacpp.LLVM.LLVMBuildNeg;

import java.util.Set;

public class NegExpr extends Expr {
    private final Expr expr;

    public NegExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public LLVM.LLVMValueRef codeGen(CodeGen codegen) {
        return LLVMBuildNeg(codegen.getBuilder(), expr.codeGen(codegen), "");
    }

    @Override
    public Set<String> getIdentifiers() {
        return expr.getIdentifiers();
    }

    @Override
    public String toString() {
        return "-" + expr.toString();
    }
}
