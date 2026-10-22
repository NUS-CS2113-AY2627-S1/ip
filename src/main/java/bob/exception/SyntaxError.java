package bob.exception;

/**
 * Signals that a command does not match the supported syntax.
 */
public class SyntaxError extends Exception {
    private static final String ERROR_MESSAGE = "You bum, I don't know what that means";

    /**
     * Creates an exception with the existing invalid-syntax message.
     */
    public SyntaxError() {
        super(ERROR_MESSAGE);
    }
}
