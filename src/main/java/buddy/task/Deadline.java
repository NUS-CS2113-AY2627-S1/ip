package buddy.task;

/**
 * Represents a task that should be completed by a specific date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description text that describes the task
     * @param by deadline for the task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the icon that identifies this task type.
     *
     * @return deadline task icon
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the display text for this deadline task.
     *
     * @return task status, description, and deadline
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns this deadline encoded for storage, with the due date appended
     * after the fields common to all tasks.
     *
     * @return this deadline encoded for storage
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + by;
    }
}
