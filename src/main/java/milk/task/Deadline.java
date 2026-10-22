package milk.task;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Constructor of the Deadline task.
     * @param description The Deadline's description.
     * @param by The Deadline's deadline.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Gets the Deadline's deadline.
     * @return Deadline's deadline.
     */
    public String getBy() {
        return by;
    }

    /**
     * Sets the Deadline's deadline.
     * @param by Deadline's deadline.
     */
    public void setBy(String by) {
        this.by = by;
    }

    /**
     * Getter of the Deadline task icon.
     * Overrides the parent class's function.
     * @return Deadline task icon.
     */
    @Override
    public String getTaskIcon() {
        return "[D]";
    }

    /**
     * Overrides the toString function.
     * @return The task icon, followed by the status icon, followed by the description, followed by its deadline.
     */
    @Override
    public String toString() {
        return getTaskIcon() + getStatusIcon() + " " + super.toString() + " (by: " + this.by + ")";
    }
}
