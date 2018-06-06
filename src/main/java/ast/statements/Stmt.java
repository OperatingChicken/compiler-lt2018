package ast.statements;

import java.util.Set;

public abstract class Stmt {
    public abstract void codeGen();

    public abstract Set<String> getIdentifiers();

    @Override
    public abstract String toString();
}
