package jamal.task;

/**
 * A task that needs to be done before a specific date/time,
 * e.g., submit report by 11/10/2019 5pm.
 */
public class Deadline extends Task {
    /** Time the task is due, as typed by the user. */
    private String by;

    /**
     * Creates a deadline with the given description and due time.
     *
     * @param description Text of the task.
     * @param by When the task is due, as typed by the user.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns when the task is due.
     *
     * @return Due time, as typed by the user.
     */
    public String getBy() {
        return by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
