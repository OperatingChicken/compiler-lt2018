package ast.statements;

public abstract class Stmt {
    abstract void genCode();

    @Override
    public abstract String toString();
}
