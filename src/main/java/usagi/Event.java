package usagi;

/**
 * Represents a task that starts at a specific date or time and ends at another.
 */
public class Event extends Task {
    /** Date or time the event starts, kept as free-form text. */
    protected String from;

    /** Date or time the event ends, kept as free-form text. */
    protected String to;

    /**
     * Creates a new, not-done event spanning the given start and end times.
     *
     * @param description Text describing the event.
     * @param from Date or time the event starts, e.g. {@code "Mon 2pm"}.
     * @param to Date or time the event ends, e.g. {@code "4pm"}.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + Storage.FIELD_SEPARATOR + from
                + Storage.FIELD_SEPARATOR + to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
