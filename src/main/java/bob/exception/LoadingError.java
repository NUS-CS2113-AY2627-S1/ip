package bob.exception;

/**
 * Signals that the saved task file could not be read.
 */
public class LoadingError extends Exception {
    private static final String ERROR_MESSAGE = "I couldn't load your saved tasks.";

    /**
     * Creates an exception with the existing error message.
     */
    public LoadingError() {
        super(ERROR_MESSAGE);
    }

    /**
     * Creates an exception with the existing message and original cause.
     *
     * @param cause underlying failure retained for debugging.
     */
    public LoadingError(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
