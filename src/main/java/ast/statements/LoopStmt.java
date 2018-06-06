package ast.statements;

import ast.expressions.Expr;

public class LoopStmt extends Stmt {
    private final Expr condition;
    private final Stmt body;

    public LoopStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    void genCode() {

    }

    @Override
    public String toString() {
        return "loop " + condition.toString() +
                System.lineSeparator() + body.toString() +
                System.lineSeparator() + "endLoop";
    }
}
