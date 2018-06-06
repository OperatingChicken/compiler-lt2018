package ast.expressions;

import java.util.Set;

public abstract class Expr {
    //public abstract void codeGen();

    public abstract Set<String> getIdentifiers();

    @Override
    public abstract String toString();
}
