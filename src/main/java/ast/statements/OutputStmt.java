package ast.statements;

import ast.expressions.Expr;

public class OutputStmt extends Stmt {
    private final String label;
    private final Expr expr;

    public OutputStmt(String label, Expr expr) {
        this.label = label;
        this.expr = expr;
    }

    public OutputStmt(String label) {
        this(label, null);
    }

    public OutputStmt(Expr expr) {
        this("", expr);
    }

    public OutputStmt() {
        this(System.lineSeparator(), null);
    }

    @Override
    void genCode() {

    }

    @Override
    public String toString() {
        return (label.equals(System.lineSeparator()) && expr == null) ?
                "newLine" : ("output \"" + label + "\" " + expr.toString());
    }
}
