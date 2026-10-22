package eric.task;

/**
 * A task that must be done by a given time, e.g. "deadline return book /by Sunday".
 */
public class Deadline extends Task {
    /** One-letter tag of this task type, used in the list and in the save file. */
    public static final String TYPE_ICON = "D";

    /** When the task must be done, as free text. */
    protected String by;

    /**
     * Creates a deadline task.
     *
     * @param description What the task is about.
     * @param by When the task must be done.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + FILE_SEPARATOR + by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
