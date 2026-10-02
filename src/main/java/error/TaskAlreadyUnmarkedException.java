package error;

/** Signals an attempt to unmark a task that is already incomplete. */
public class TaskAlreadyUnmarkedException extends Exception {
    /** Creates an exception for unmarking an incomplete task. */
    public TaskAlreadyUnmarkedException() {
        super();
    }
}
