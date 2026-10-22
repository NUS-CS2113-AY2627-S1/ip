package nga;

/** Indicates that a user command does not match the expected task format. */
public class TaskParseException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a user-facing explanation of the invalid format.
     *
     * @param message the explanation to display to the user
     */
    public TaskParseException(String message) {
        super(message);
    }
}
