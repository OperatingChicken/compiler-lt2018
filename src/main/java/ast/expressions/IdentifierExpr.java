package ast.expressions;

public class IdentifierExpr extends Expr {
    private final String identifier;

    public IdentifierExpr(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
