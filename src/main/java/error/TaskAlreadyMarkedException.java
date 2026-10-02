package error;

/** Signals an attempt to mark a task that is already marked as complete. */
public class TaskAlreadyMarkedException extends Exception {
    /** Creates an exception for marking an already completed task. */
    public TaskAlreadyMarkedException() {
        super();
    }
}
