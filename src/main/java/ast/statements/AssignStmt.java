package ast.statements;

import java.util.HashSet;
import java.util.Set;
import ast.expressions.Expr;
import cg.CodeGen;

public class AssignStmt extends Stmt {
    private final String identifier;
    private final Expr expr;

    public AssignStmt(String identifier, Expr expr) {
        this.identifier = identifier;
        this.expr = expr;
    }

    @Override
    public void codeGen(CodeGen codegen) {
        codegen.setVariable(this.identifier, this.expr.codeGen(codegen));
    }

    @Override
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<>(this.expr.getIdentifiers());
        result.add(this.identifier);
        return result;
    }

    @Override
    public String toString() {
        return identifier + " = " + expr;
    }
}
