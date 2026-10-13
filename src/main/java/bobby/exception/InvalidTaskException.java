package bobby.exception;

/**
 * Thrown when a task or task-related input is invalid.
 */
public class InvalidTaskException extends BobbyException {

    /**
     * Creates an InvalidTaskException with the given error message.
     *
     * @param message the error message
     */
    public InvalidTaskException(String message) {
        super(message);
    }
}
