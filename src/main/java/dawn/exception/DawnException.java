package dawn.exception;

/**
 * Reports an expected application error with a user-facing explanation.
 */
public class DawnException extends Exception {
    /**
     * Constructs a core application exception with a message.
     *
     * @param message the user-facing error explanation.
     */
    public DawnException(String message) {
        super(message);
    }
}
