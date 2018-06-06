package ast.statements;

import java.util.Set;
import cg.CodeGen;

public abstract class Stmt {
    public abstract void codeGen(CodeGen codegen);
    public abstract Set<String> getIdentifiers();

    @Override
    public abstract String toString();
}
