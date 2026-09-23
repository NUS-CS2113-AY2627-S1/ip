package lumi.task;

/** Represents a task that spans a period between two stated points in time. */
public class Event extends Task {
    private static final String TYPE_ICON = "E";

    protected final String from;
    protected final String to;

    /**
     * Creates an event.
     *
     * @param description What the event is.
     * @param from When it starts, as the user wrote it.
     * @param to When it ends, as the user wrote it.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the letter that marks an event in the listing. */
    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }

    /** Returns the shared fields followed by the start and end times. */
    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + FIELD_SEPARATOR + from + FIELD_SEPARATOR + to;
    }

    /** Returns the listing form with the period in brackets. */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
