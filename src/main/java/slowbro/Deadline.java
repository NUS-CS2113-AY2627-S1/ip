package slowbro;

/** Represents a task that must be completed by a specified date or time. */
public class Deadline extends Task {
    private static final String DEADLINE_PREFIX = "[D]";
    private static final String DEADLINE_FORMAT = " (by: %s)";

    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the date or time by which the task should be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Returns the deadline details. */
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return DEADLINE_PREFIX + super.toString()
                + String.format(DEADLINE_FORMAT, by);
    }
}
