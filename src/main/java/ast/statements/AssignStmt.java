package ast.statements;

import ast.expressions.Expr;

public class AssignStmt extends Stmt {
    private final String identifier;
    private final Expr expr;

    public AssignStmt(String identifier, Expr expr) {
        this.identifier = identifier;
        this.expr = expr;
    }

    @Override
    public void genCode() {

    }

    @Override
    public String toString() {
        return identifier + " = " + expr;
    }
}
