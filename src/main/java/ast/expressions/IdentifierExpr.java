package ast.expressions;

import java.util.Set;
import java.util.HashSet;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import cg.CodeGen;

public class IdentifierExpr extends Expr {
    private final String identifier;

    public IdentifierExpr(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Set<String> getIdentifiers() {
        HashSet<String> result = new HashSet<>();
        result.add(this.identifier);
        return result;
    }

    @Override
    public LLVMValueRef codeGen(CodeGen codegen) {
        return codegen.getVariable(this.identifier);
    }

    @Override
    public String toString() {
        return identifier;
    }
}
