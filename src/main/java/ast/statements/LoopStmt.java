package ast.statements;

import java.util.Set;
import ast.expressions.Expr;
import cg.CodeGen;

public class LoopStmt extends Stmt {
    private final Expr condition;
    private final Stmt body;

    public LoopStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    public void codeGen(CodeGen codegen) {
        System.err.println("LoopStmt UNIMPLEMENTED!!!");
        System.exit(1);
    }

    public Set<String> getIdentifiers() {
        Set<String> result = this.condition.getIdentifiers();
        result.addAll(this.body.getIdentifiers());
        return result;
    }

    @Override
    public String toString() {
        return "loop " + condition.toString() +
                System.lineSeparator() + body.toString() +
                System.lineSeparator() + "endLoop";
    }
}
