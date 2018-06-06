package ast.expressions;

import java.util.Set;
import java.util.HashSet;

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

    @Override
    public String toString() {
        return "input \"" + prompt + "\"";
    }
}
