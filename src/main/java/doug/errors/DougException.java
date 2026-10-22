package doug.errors;

/**
 * Represents an application error that can be reported to the user by Doug.
 */
public class DougException extends RuntimeException {
    /**
     * Creates an exception with a user-facing explanation of the error.
     *
     * @param message explanation of the error
     */
    public DougException(String message) {
        super(message);
    }
}
