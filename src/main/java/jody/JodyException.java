package jody;

/**
 * Reports a command, task, date-parsing, or storage error to Jody.
 */
public class JodyException extends Exception {
    /**
     * Creates an exception with a user-facing error message.
     *
     * @param message the error explanation
     */
    public JodyException(String message) {
        super(message);
    }

    /**
     * Creates an exception that preserves the underlying cause.
     *
     * @param message the error explanation
     * @param cause the underlying error
     */
    public JodyException(String message, Throwable cause) {
        super(message, cause);
    }
}
