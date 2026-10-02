package dog.task;

/**
 * Represents a task that occurs between specified start and end times.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event with the given description and time range.
     *
     * @param description Description of the event.
     * @param from Start time supplied by the user.
     * @param to End time supplied by the user.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date or time as free-form text.
     *
     * @return Start date or time supplied by the user.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end date or time as free-form text.
     *
     * @return End date or time supplied by the user.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the event type, status, description, and time range.
     *
     * @return Display text beginning with {@code [E]}.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
