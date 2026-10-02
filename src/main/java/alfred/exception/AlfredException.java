package alfred.exception;

/**
 * Represents an error caused by an invalid Alfred command.
 */
public class AlfredException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an Alfred-specific exception with an explanatory message.
     *
     * @param message Explanation of the error.
     */
    public AlfredException(String message) {
        super(message);
    }
}
