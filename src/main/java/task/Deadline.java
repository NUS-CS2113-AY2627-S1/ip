package task;

/**
 * Represents a deadline task that needs to be done by a specific date/time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Constructs a new Deadline task with the given description and due date.
     *
     * @param description The description of the task.
     * @param by The deadline as a string.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline of this task.
     *
     * @return The deadline string.
     */
    public String getBy() {
        return by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }

    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
