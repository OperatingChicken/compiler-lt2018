package ast.expressions;

import java.util.Set;
import java.util.HashSet;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import cg.CodeGen;

public class NumLiteralExpr extends Expr {
    private final Long value;

    public NumLiteralExpr(Long value) {
        this.value = value;
    }

    public Set<String> getIdentifiers() {
        return new HashSet<>();
    }

    public LLVMValueRef codeGen(CodeGen codegen) {
        return codegen.getConstant(this.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
