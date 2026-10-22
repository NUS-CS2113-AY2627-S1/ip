package cracker;

/**
 * Represents an error caused by an invalid Cracker command.
 */
public class CrackerException extends Exception {
    /** Version identifier for serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a user-facing explanation of the invalid command.
     *
     * @param message explanation and correction for the invalid command
     */
    public CrackerException(String message) {
        super(message);
    }
}
