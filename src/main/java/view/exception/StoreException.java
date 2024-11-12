package view.exception;

public class StoreException extends IllegalArgumentException {
    private static final String ERROR_PREFIX = "[ERROR] ";

    public StoreException(String message) {
        super(ERROR_PREFIX + message);
    }
}
