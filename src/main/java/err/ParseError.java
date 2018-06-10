package err;

public class ParseError extends AbstractError {
    public ParseError(int line, int column) {
        super("", line, column);
    }

    public ParseError(int line, int column, String expectedTokens) {
        super("Expected one of " + expectedTokens, line, column);
    }

    @Override
    String getErrorName() {
        return "Parse";
    }
}
