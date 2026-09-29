/**
 * A task that must be done by a certain time.
 */
public class Deadline extends Task {
    /** When the task is due, as typed by the user. */
    protected String by;

    /**
     * Creates a deadline that is not done yet.
     *
     * @param by when the task is due, as typed by the user (e.g. "Sunday").
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Formats the deadline as, e.g., "[D][ ] return book (by: Sunday)". */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
