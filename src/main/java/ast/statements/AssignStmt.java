package ast.statements;

import java.util.Set;
import ast.expressions.Expr;

public class AssignStmt extends Stmt {
    private final String identifier;
    private final Expr expr;

    public AssignStmt(String identifier, Expr expr) {
        this.identifier = identifier;
        this.expr = expr;
    }

    @Override
    public void codeGen() {

    }

    public Set<String> getIdentifiers() {
        Set<String> result = this.expr.getIdentifiers();
        result.add(this.identifier);
        return result;
    }

    @Override
    public String toString() {
        return identifier + " = " + expr;
    }
}
