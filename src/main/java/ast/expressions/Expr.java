package ast.expressions;

import java.util.Set;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import cg.CodeGen;

public abstract class Expr {
    public abstract LLVMValueRef codeGen(CodeGen codegen);
    public abstract Set<String> getIdentifiers();

    @Override
    public abstract String toString();
}
