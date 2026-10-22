package usagi;

/**
 * Represents a task that has to be done before a specific date or time.
 */
public class Deadline extends Task {
    /**
     * The date or time this task is due by, kept as free-form text because
     * dates are not parsed into date objects at this stage of the project.
     */
    protected String by;

    /**
     * Creates a new, not-done deadline with the given description and due date.
     *
     * @param description Text describing the deadline.
     * @param by Date or time the task is due by, e.g. {@code "Sunday"}.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + Storage.FIELD_SEPARATOR + by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
