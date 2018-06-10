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

    @Override
    public void codeGen(CodeGen codegen) {
        codegen.outputString(this.label);
        if(this.expr != null) {
            codegen.outputValue(this.expr.codeGen(codegen));
        }
    }

    @Override
    public Set<String> getIdentifiers() {
        Set<String> result = new HashSet<>();
        if(this.expr != null) {
            result.addAll(this.expr.getIdentifiers());
        }
        return result;
    }

    @Override
    public String toString() {
        return (label.equals(System.lineSeparator()) && expr == null) ?
                "newLine" : ("output \"" + label + "\"" + (expr == null ? "" : " " + expr.toString()));
    }
}
