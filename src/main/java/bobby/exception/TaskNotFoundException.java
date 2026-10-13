package bobby.exception;

/**
 * Thrown when a requested task cannot be found.
 */
public class TaskNotFoundException extends BobbyException {

    /**
     * Creates a TaskNotFound exception with the given error message.
     *
     * @param message the error message
     */
    public TaskNotFoundException(String message) {
        super(message);
    }
}
