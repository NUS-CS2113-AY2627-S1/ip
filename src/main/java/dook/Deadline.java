package dook;

/**
 * Represents a task that has a specified deadline.
 */
public class Deadline extends Task {
    private String by;

    /**
     * Creates a new deadline task with the specified description and deadline.
     *
     * @param description the description of the deadline task
     * @param by the deadline date or time
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline of this task.
     *
     * @return the deadline date or time
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns a string representation of this deadline task.
     *
     * @return the deadline task with its type, completion status, and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
