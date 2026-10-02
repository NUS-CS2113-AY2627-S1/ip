package dog.task;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline with the given description and due time.
     *
     * @param description Description of the deadline.
     * @param by Due time supplied by the user.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date or time as free-form text.
     *
     * @return Due date or time supplied by the user.
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns the deadline type, status, description, and due time.
     *
     * @return Display text beginning with {@code [D]}.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
