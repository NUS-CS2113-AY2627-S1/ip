package bob.exception;

/**
 * Signals that a task index is outside the list.
 */
public class IndexOutOfBoundsError extends Exception {
    private static final String ERROR_MESSAGE = "No such item exists at the specified index";

    /**
     * Creates an exception with the existing invalid-index message.
     */
    public IndexOutOfBoundsError() {
        super(ERROR_MESSAGE);
    }
}
