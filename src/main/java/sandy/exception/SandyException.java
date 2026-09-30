package sandy.exception;

/**
 * Represents an error caused by an invalid command entered for Sandy.
 */
public class SandyException extends Exception {
    /**
     * Creates an exception with the given user-facing message.
     *
     * @param message The explanation of the command error.
     */
    public SandyException(String message) {
        super(message);
    }
}
