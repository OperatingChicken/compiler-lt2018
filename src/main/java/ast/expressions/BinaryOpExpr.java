package ast.expressions;

public abstract class BinaryOpExpr extends Expr {
    private final Expr leftOperand;
    private final Expr rightOperand;

    BinaryOpExpr(Expr leftOperand, Expr rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    abstract char getOperator();

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + " " + getOperator() + " " + rightOperand.toString() + ")";
    }
}
