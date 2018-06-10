package ast.expressions;

import cg.CodeGen;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;

import java.util.HashSet;
import java.util.Set;

public class AssignExpr extends Expr {
    private final String identifier;
    private final Expr expr;

    public AssignExpr(String identifier, Expr expr) {
        this.identifier = identifier;
        this.expr = expr;
    }

    @Override
    public LLVMValueRef codeGen(CodeGen codegen) {
        codegen.setVariable(this.identifier, this.expr.codeGen(codegen));
        return codegen.getVariable(this.identifier);
    }

    @Override
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<>(this.expr.getIdentifiers());
        result.add(this.identifier);
        return result;
    }

    @Override
    public String toString() {
        return "(" + identifier + " = " + expr + ")";
    }
}
