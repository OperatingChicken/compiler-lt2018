package ast.statements;

import java.util.Set;
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
    public void codeGen() {

    }

    public Set<String> getIdentifiers() {
        return this.expr.getIdentifiers();
    }

    @Override
    public String toString() {
        return (label.equals(System.lineSeparator()) && expr == null) ?
                "newLine" : ("output \"" + label + "\" " + expr.toString());
    }
}
