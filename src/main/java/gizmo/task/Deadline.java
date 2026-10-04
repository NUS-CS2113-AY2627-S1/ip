package gizmo.task;

/**
 * A task that must be completed by a specified date or time.
 */
public class Deadline extends Task {

    protected String completeBy;

    /** Creates a deadline task with its description and completion time. */
    public Deadline(String description, String completeBy) {
        super(description);
        this.completeBy = completeBy;
    }

    /** Returns the deadline date or time. */
    public String getCompleteBy() {
        return completeBy;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + completeBy + ")";
    }
}
