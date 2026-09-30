package xxlilchatxx;

/**
 * Represents a task that must be done before a specific date/time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a new deadline task with the given description and due date/time.
     *
     * @param description Description of the task.
     * @param by Date/time by which the task should be done.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + description + " | " + by;
    }
}
