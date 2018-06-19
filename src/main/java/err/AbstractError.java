package err;

public abstract class AbstractError extends Exception {
    private int line;
    private int column;

    AbstractError(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
    }

    abstract String getErrorName();

    @Override
    public final String getMessage() {
        String result = String.format("%s error at line %d, column %d", getErrorName(), line, column);
        String details = super.getMessage();
        if (details != null && details.length() != 0) {
            result += ": " + details;
        }
        return result;
    }
}
