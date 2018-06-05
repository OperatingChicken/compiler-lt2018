public class AssignStmt extends Stmt {
    private String identifier;
    private Expr expr;

    public AssignStmt(String identifier, Expr expr) {
        this.identifier = identifier;
        this.expr = expr;
    }

    @Override
    public void genCode() {

    }

    @Override
    public String toString() {
        return identifier + " = " + expr;
    }
}
