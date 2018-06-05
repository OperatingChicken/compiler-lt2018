public class SubExpr extends BinaryOpExpr {
    public SubExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    char getOperator() {
        return '-';
    }
}
