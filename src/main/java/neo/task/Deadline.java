package neo.task;

/**
 * Represents a task that needs to be completed before a specific date or time.
 * Inherits from the base Task class and adds a deadline attribute.
 */
public class Deadline extends Task {
    /** The deadline date and time for the task. */
    private String by;

    /**
     * Creates an incomplete deadline task with the specified description and deadline time.
     *
     * @param description Text that describes the task.
     * @param by The date and time the task needs to be completed by.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns a string representation of this deadline task for UI display.
     *
     * @return A formatted string including the task type, status, description, and deadline.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns a string representation of this deadline task for saving to a persistent file.
     *
     * @return A pipe-separated record with escaped, readable text fields.
     */
    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + escapeForStorage(by);
    }
}
