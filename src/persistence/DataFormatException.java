package persistence;

public class DataFormatException extends Exception {

    private final int lineNumber;

    public DataFormatException(int lineNumber, String message) {
        this(lineNumber, message, null);
    }

    public DataFormatException(int lineNumber, String message, Throwable cause) {
        super(message, cause);
        if (lineNumber <= 0 || message == null || message.isBlank()) {
            throw new IllegalArgumentException("A positive line number and reason are required.");
        }
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
