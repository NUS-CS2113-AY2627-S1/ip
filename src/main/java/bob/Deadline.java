package bob;

/**
 * Represents a task that must be completed by a deadline.
 */
public class Deadline extends Task {

    /** Deadline description for this task. */
    protected String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description Description of task.
     * @param by Deadline description.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in the format used for saving data.
     *
     * @return Serialized deadline data.
     */
    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + Storage.escapeField(description)
            + " | " + Storage.escapeField(by);
    }

    /**
     * Returns the display representation of this deadline.
     *
     * @return The deadline type, completion status, description, and due date.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
