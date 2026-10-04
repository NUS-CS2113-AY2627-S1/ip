package dook.exception;

/**
 * Represents the exception when user gives invalid task index.
 */
public class TaskListIndexOutOfBoundsException extends DookException {
    public TaskListIndexOutOfBoundsException() {
        super("You reach into the void. That task does not exist.");
    }
}
