package zack;

/**
 * Represents an invalid command or another expected task-manager error.
 */
public class ZackException extends Exception {
    /**
     * Creates an exception with a message suitable for display to the user.
     *
     * @param message explanation of the error
     */
    public ZackException(String message) {
        super(message);
    }
}