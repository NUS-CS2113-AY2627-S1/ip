package bigbrother.task;

/**
 * Represents a task that takes place during a specified time range.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with the given description and time range.
     *
     * @param description the text that describes the event
     * @param from the starting time of the event
     * @param to the ending time of the event
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the starting time of this event.
     *
     * @return the event's starting time
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the ending time of this event.
     *
     * @return the event's ending time
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the formatted representation of this event.
     *
     * @return the event type, status, description, and time range
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + getDescription()
                + " (from: " + from + " to: " + to + ")";
    }
}
