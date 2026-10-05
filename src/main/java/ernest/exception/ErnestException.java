package ernest.exception;

/**
 * Represents an expected error that Ernest can explain to the user.
 */
public final class ErnestException extends Exception {
    /**
     * Creates an Ernest exception with a user-facing explanation.
     *
     * @param message explanation of the error.
     */
    public ErnestException(String message) {
        super(message);
    }
}
