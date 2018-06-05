package ast.expressions;

public class InputExpr extends Expr {
    private final String prompt;

    public InputExpr(String prompt) {
        this.prompt = prompt;
    }

    public InputExpr() {
        this("");
    }

    @Override
    public String toString() {
        return "input \"" + prompt + "\"";
    }
}
