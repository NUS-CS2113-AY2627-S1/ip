package clammy.exception;

/**
 * Indicates that a user-supplied task number does not identify a stored task.
 */
public class TaskNotFoundException extends ClammyException {
    /**
     * Creates an error explaining that the requested task does not exist.
     */
    public TaskNotFoundException() {
        super("That task number does not exist.");
    }
}
