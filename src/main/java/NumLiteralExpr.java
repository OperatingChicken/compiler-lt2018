public class NumLiteralExpr extends Expr {
    private Long value;

    public NumLiteralExpr(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
