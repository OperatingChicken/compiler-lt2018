public abstract class BinaryOpExpr extends Expr {
    private Expr leftOperand;
    private Expr rightOperand;

    public BinaryOpExpr(Expr leftOperand, Expr rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    abstract char getOperator();

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + " " + getOperator() + " " + rightOperand.toString() + ")";
    }
}
