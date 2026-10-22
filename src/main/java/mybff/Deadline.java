package mybff;

/** Represents a task that must be completed by a date or time. */
public class Deadline extends Task {
    private final String by;

    /** Creates a deadline. */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Returns the deadline type marker. */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /** Returns the stored task fields with the encoded deadline appended. */
    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + encodeField(by);
    }

    /** Returns the task's display representation, including its deadline. */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
