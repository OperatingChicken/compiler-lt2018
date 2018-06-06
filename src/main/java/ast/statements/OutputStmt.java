package ast.statements;

import java.util.Set;
import java.util.HashSet;
import ast.expressions.Expr;
import cg.CodeGen;

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

    public void codeGen(CodeGen codegen) {
        codegen.outputString(this.label);
        if(this.expr != null) {
            codegen.outputValue(this.expr.codeGen(codegen));
        }
    }

    public Set<String> getIdentifiers() {
        if(this.expr == null) {
            return new HashSet<>();
        }
        return this.expr.getIdentifiers();
    }

    @Override
    public String toString() {
        return (label.equals(System.lineSeparator()) && expr == null) ?
                "newLine" : ("output \"" + label + "\" " + expr.toString());
    }
}
