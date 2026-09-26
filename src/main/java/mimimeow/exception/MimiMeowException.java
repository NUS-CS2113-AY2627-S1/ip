package mimimeow.exception;

/** Represents an application error that can be presented to the user. */
public abstract class MimiMeowException extends RuntimeException {
    /**
     * Creates an exception with the specified user-facing message.
     *
     * @param message user-facing error message
     */
    protected MimiMeowException(String message) {
        super(message);
    }

    /**
     * Creates an exception with a user-facing message and its underlying cause.
     *
     * @param message user-facing error message
     * @param cause underlying cause
     */
    protected MimiMeowException(String message, Throwable cause) {
        super(message, cause);
    }
}
