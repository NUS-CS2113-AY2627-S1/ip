package ace.exception;

/**
 * Signals an error that Ace can report to the user.
 */
public class AceException extends Exception {

    /**
     * Creates an exception with a user-facing error message.
     *
     * @param message message explaining the error
     */
    public AceException(String message) {
        super(message);
    }
}
