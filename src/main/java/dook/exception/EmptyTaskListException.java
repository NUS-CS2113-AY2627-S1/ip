package dook.exception;

/**
 * Represents the exception when attempting to view an empty task list.
 */
public class EmptyTaskListException extends DookException {
    public EmptyTaskListException() {
        super("You are alone in this darkness.");
    }
}
