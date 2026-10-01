package proton.exception;

/**
 * Represents a recoverable error caused by an invalid command given to Proton.
 */
public class ProtonException extends Exception {
    /**
     * Creates an exception with an explanation suitable for showing to the user.
     *
     * @param message Explanation of the invalid command.
     */
    public ProtonException(String message) {
        super(message);
    }
}
