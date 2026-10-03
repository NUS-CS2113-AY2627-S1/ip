package clammy.task;

/**
 * Represents a task that has a date range.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event occurring over a specified period.
     *
     * @param description Description of the event.
     * @param from Start of the event's period.
     * @param to End of the event's period.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start text.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event end text.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the event type, completion status, description, and time range.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
