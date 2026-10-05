package ted.task;

/**
 * Represents a task that takes place between specified start and end dates or times.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with the given description, start, and end.
     *
     * @param description The event description.
     * @param from The date or time at which the event starts.
     * @param to The date or time at which the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the date or time at which this event starts.
     *
     * @return The event start as entered by the user.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the date or time at which this event ends.
     *
     * @return The event end as entered by the user.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the display form of this event.
     *
     * @return The event type, completion status, description, start, and end.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

