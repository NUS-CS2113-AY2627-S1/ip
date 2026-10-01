package goober;

/**
 * Represents an expected error caused by invalid Goober input.
 */
public class GooberException extends Exception {
    /**
     * Creates an exception with a user-facing error message.
     *
     * @param message Error message to display.
     */
    public GooberException(String message) {
        super(message);
    }
}
