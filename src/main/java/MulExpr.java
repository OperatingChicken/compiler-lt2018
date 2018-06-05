public class MulExpr extends BinaryOpExpr {
    public MulExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    char getOperator() {
        return '*';
    }
}
