package vani.task;

/**
 * Represents a deadline task that has a description and a due date.
 */
public class Deadline extends Task {

    private String by;

    /**
     * Creates a new incomplete deadline task with the supplied description and
     * due date.
     *
     * @param description the text entered for the task
     * @param by the due date entered for the task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date as the text supplied by the user.
     *
     * @return the due date text
     */
    public String getBy() {
        return by;
    }

    /**
     * Replaces the due date text without changing the other task details.
     *
     * @param by the new due date text
     */
    public void setBy(String by) {
        this.by = by;
    }

    /**
     * Returns the deadline's type, completion status, description, and due date for display.
     *
     * @return the task details prefixed with {@code [D]} and followed by the due date
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + getDescription() + " (by: " + by + ")";
    }
}
