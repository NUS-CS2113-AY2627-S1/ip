package duke;

/**
 * Represents a task that must be done before a specific date/time.
 */
public class Deadline extends Task {

    protected String by;

    /**
     * Creates a new deadline task.
     *
     * @param description What the task is about.
     * @param by          When the task is due.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}