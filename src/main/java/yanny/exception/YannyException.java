package yanny.exception;

/**
 * Represents an expected error caused by invalid user input.
 */
public class YannyException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a user-facing error message.
     *
     * @param message the error message to display to the user.
     */
    public YannyException(String message) {
        super(message);
    }
}
