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
        return String.format("%s error at line %d, column %d: %s", getErrorName(), line, column, super.getMessage());
    }
}
