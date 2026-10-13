package bobby.exception;

/**
 * Thrown when an error occurs while creating, writing and reading
 * from the data file that stores the task list.
 */
public class StorageException extends BobbyException {

    /**
     * Creates a StorageException with the given error message.
     *
     * @param message the error message
     */
    public StorageException(String message) {
        super(message);
    }
}
