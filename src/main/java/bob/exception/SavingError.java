package bob.exception;

/**
 * Signals that the task file could not be written.
 */
public class SavingError extends Exception {
    private static final String ERROR_MESSAGE = "I couldn't save your tasks to disk.";

    /**
     * Creates an exception with the existing error message.
     */
    public SavingError() {
        super(ERROR_MESSAGE);
    }

    /**
     * Creates an exception with the existing message and original cause.
     *
     * @param cause underlying failure retained for debugging.
     */
    public SavingError(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
