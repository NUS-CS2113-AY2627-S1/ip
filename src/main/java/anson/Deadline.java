package anson;

/**
 * Represents a task that needs to be completed by a specific date/time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a deadline task with the given description and due date/time.
     *
     * @param description Description of the task.
     * @param by Date/time by which the task should be completed.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the string representation of this deadline task.
     *
     * @return Formatted string in the form "[D][status] description (by: ...)".
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns the save-file line for this deadline task.
     *
     * @return String in the form "D | isDone | description | by".
     */
    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by;
    }
}