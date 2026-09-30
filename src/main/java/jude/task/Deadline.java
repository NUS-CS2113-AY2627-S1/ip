package jude.task;

/**
 * Represents a deadline task, which is a task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Constructs a Deadline task with the specified description and deadline time.
     *
     * @param description The description of the deadline task.
     * @param by The date/time by which the task needs to be done.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns a string representation of the deadline task for displaying to the user.
     *
     * @return A string representation of the deadline task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns a string representation of the deadline task formatted for saving to a file.
     *
     * @return A string representation of the deadline task for file storage.
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }
}
