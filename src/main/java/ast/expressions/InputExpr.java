package ast.expressions;

import java.util.Set;
import java.util.HashSet;
import org.bytedeco.javacpp.LLVM.LLVMValueRef;
import cg.CodeGen;

public class InputExpr extends Expr {
    private final String prompt;

    public InputExpr(String prompt) {
        this.prompt = prompt;
    }

    public InputExpr() {
        this("");
    }

    public Set<String> getIdentifiers() {
        return new HashSet<>();
    }

    public LLVMValueRef codeGen(CodeGen codegen) {
        codegen.outputString(this.prompt);
        return codegen.inputValue();
    }

    @Override
    public String toString() {
        return "input \"" + prompt + "\"";
    }
}
