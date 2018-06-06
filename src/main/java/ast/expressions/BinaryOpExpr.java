package ast.expressions;

import java.util.Set;

public abstract class BinaryOpExpr extends Expr {
    private final Expr leftOperand;
    private final Expr rightOperand;

    BinaryOpExpr(Expr leftOperand, Expr rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Set<String> getIdentifiers() {
        Set<String> result = this.leftOperand.getIdentifiers();
        result.addAll(this.rightOperand.getIdentifiers());
        return result;
    }

    abstract char getOperator();

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + " " + getOperator() + " " + rightOperand.toString() + ")";
    }
}
