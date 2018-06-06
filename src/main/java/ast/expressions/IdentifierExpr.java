package ast.expressions;

import java.util.Set;
import java.util.HashSet;

public class IdentifierExpr extends Expr {
    private final String identifier;

    public IdentifierExpr(String identifier) {
        this.identifier = identifier;
    }

    public Set<String> getIdentifiers() {
        HashSet<String> result = new HashSet<>();
        result.add(this.identifier);
        return result;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
