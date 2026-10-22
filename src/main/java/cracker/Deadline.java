package cracker;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** Text describing when the task is due. */
    private final String by;

    /**
     * Creates an incomplete deadline task with the given description and due time.
     *
     * @param description text describing the task
     * @param by text describing when the task is due
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline text for this task.
     *
     * @return the deadline text
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns this deadline task in the display format.
     *
     * @return this task's type, status, description, and due time
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
