package ast.expressions;

public class NumLiteralExpr extends Expr {
    private final Long value;

    public NumLiteralExpr(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
