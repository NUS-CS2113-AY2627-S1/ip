package task;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task with its description and due time.
     *
     * @param description Description of the deadline task.
     * @param by Due time of the task.
     */
    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /** Returns this deadline in the format used for persistent storage. */
    /**
     * Returns this deadline in the format used for persistent storage.
     *
     * @return Serialized deadline data.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeFileField(by);
    }

    /** Returns a display-friendly representation of this deadline. */
    /**
     * Returns a display-friendly representation of this deadline.
     *
     * @return Display text for this deadline.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
