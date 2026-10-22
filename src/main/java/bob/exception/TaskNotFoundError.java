package bob.exception;

/**
 * Signals that no task description matches the search keyword.
 */
public class TaskNotFoundError extends Exception {
    private static final String ERROR_MESSAGE = "No tasks found matching that keyword.";

    /**
     * Creates an exception with the no-matching-tasks message.
     */
    public TaskNotFoundError() {
        super(ERROR_MESSAGE);
    }
}
