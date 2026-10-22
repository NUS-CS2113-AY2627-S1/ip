package bob;

/**
 * Represents an error caused by invalid commands or task data.
 */
public class BobException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with an explanatory message.
     *
     * @param message Explanation of the error.
     */
    public BobException(String message) {
        super(message);
    }
}
