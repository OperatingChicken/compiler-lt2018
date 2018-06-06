package ast.expressions;

import java.util.Set;
import java.util.HashSet;

public class NumLiteralExpr extends Expr {
    private final Long value;

    public NumLiteralExpr(Long value) {
        this.value = value;
    }

    public Set<String> getIdentifiers() {
        return new HashSet<>();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
