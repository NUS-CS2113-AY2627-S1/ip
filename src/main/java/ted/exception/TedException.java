package ted.exception;

/**
 * Represents an error caused by an invalid command or command argument.
 */
public class TedException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an error with the given message.
     *
     * @param message The explanation shown to the user.
     */
    public TedException(String message) {
        super(message);
    }
}

