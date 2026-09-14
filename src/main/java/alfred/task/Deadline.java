package alfred.task;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {

    protected final String by;

    /**
     * Creates an incomplete deadline with the given description and due date or
     * time.
     *
     * @param description Description of the deadline.
     * @param by Date or time by which the task must be completed.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toDataString() {
        return "D | " + super.toDataString() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
