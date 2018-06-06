package ast.expressions;

public class ModExpr extends BinaryOpExpr {
    public ModExpr(Expr leftOperand, Expr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    char getOperator() {
        return '%';
    }
}
