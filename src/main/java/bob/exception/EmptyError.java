package bob.exception;

/**
 * Signals that a required task description is empty.
 */
public class EmptyError extends Exception {
    private static final String ERROR_MESSAGE = "BRUH!!! Todo what?.";

    /**
     * Creates an exception with the existing empty-description message.
     */
    public EmptyError() {
        super(ERROR_MESSAGE);
    }
}
