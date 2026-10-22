package mybff;

/** Represents a task with a starting and ending date or time. */
public class Event extends Task {
    private final String from;
    private final String to;

    /** Creates an event. */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the event type marker. */
    @Override
    public String getTypeIcon() {
        return "E";
    }

    /** Returns the stored task fields with the encoded start and end times appended. */
    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + encodeField(from) + " | " + encodeField(to);
    }

    /** Returns the task's display representation, including its start and end times. */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
