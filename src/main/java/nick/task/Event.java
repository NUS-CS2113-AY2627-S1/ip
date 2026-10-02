package nick.task;

/**
 * Represents an event: a task that starts and ends at specific dates or times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event with the given description, start time, and end time.
     *
     * @param description Description of the event.
     * @param from The date or time the event starts.
     * @param to The date or time the event ends.
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
    public String toDisplayString() {
        return super.toDisplayString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + " | " + from + " | " + to;
    }
}
