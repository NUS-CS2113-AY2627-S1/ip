package drpijon.exception;

/**
 * Represents an error caused by an invalid Dr. Pijon command.
 */
public class DrPijonException extends Exception {
    /**
     * Creates an exception with a user-facing error message.
     *
     * @param message explanation of the invalid command or input
     */
    public DrPijonException(String message) {
        super(message);
    }
}
