package bobby.exception;

/**
 * Represents a general exception that
 * can occur in the Bobby application.
 */
public class BobbyException extends RuntimeException {

    /**
     * Creates a BobbyException with the given error message.
     *
     * @param message the error message
     */
    public BobbyException(String message) {
        super(message);
    }
}
