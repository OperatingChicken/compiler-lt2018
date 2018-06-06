package ast.expressions;

public class DivExpr extends BinaryOpExpr {
    public DivExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    char getOperator() {
        return '/';
    }
}
