package eric.task;

/**
 * A task that spans a time range, e.g. "event project meeting /from Mon 2pm /to 4pm".
 */
public class Event extends Task {
    /** One-letter tag of this task type, used in the list and in the save file. */
    public static final String TYPE_ICON = "E";

    /** When the event starts, as free text. */
    protected String from;
    /** When the event ends, as free text. */
    protected String to;

    /**
     * Creates an event task.
     *
     * @param description What the event is about.
     * @param from When the event starts.
     * @param to When the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + FILE_SEPARATOR + from + FILE_SEPARATOR + to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
