package ast.statements;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class StmtList extends Stmt {
    private final ArrayList<Stmt> statements;

    public StmtList(Stmt first) {
        statements = new ArrayList<>();
        statements.add(first);
    }

    public void add(Stmt stmt) {
        statements.add(stmt);
    }

    @Override
    public void codeGen() {

    }

    public Set<String> getIdentifiers() {
        HashSet<String> result = new HashSet<>();
        for(Stmt stmt: this.statements) {
            result.addAll(stmt.getIdentifiers());
        }
        return result;
    }

    @Override
    public String toString() {
        return statements
                .stream()
                .map(Stmt::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
