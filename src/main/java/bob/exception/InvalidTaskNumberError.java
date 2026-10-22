package bob.exception;

/**
 * Signals that a task number is invalid or outside the list.
 */
public class InvalidTaskNumberError extends Exception {
    private static final String ERROR_MESSAGE = "Please enter a valid task number.";

    /**
     * Creates an exception with the existing error message.
     */
    public InvalidTaskNumberError() {
        super(ERROR_MESSAGE);
    }

    /**
     * Creates an exception with the existing message and original cause.
     *
     * @param cause underlying failure retained for debugging.
     */
    public InvalidTaskNumberError(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
