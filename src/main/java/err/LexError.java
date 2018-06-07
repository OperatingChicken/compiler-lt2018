package err;

public class LexError extends AbstractError {
    public LexError(int line, int column, String tok) {
        super("Unrecognized token " + tok, line, column);
    }

    @Override
    protected String getErrorName() {
        return "Syntax";
    }
}
