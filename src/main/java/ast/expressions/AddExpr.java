package ast.expressions;

public class AddExpr extends BinaryOpExpr {
    public AddExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    char getOperator() {
        return '+';
    }
}
