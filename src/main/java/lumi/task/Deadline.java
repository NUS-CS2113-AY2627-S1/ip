package lumi.task;

/** Represents a task that has to be finished before a stated point in time. */
public class Deadline extends Task {
    private static final String TYPE_ICON = "D";

    protected final String by;

    /**
     * Creates a deadline.
     *
     * @param description What has to be done.
     * @param by When it is due, as the user wrote it.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Returns the letter that marks a deadline in the listing. */
    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }

    /** Returns the shared fields followed by the due date. */
    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + FIELD_SEPARATOR + by;
    }

    /** Returns the listing form with the due date in brackets. */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
