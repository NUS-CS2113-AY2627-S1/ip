package alfred.task;

/**
 * Represents a task that occurs between specified start and end times.
 */
public class Event extends Task {

    protected final String from;
    protected final String to;

    /**
     * Creates an incomplete event with the given description and time range.
     *
     * @param description Description of the event.
     * @param from Start date or time of the event.
     * @param to End date or time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
