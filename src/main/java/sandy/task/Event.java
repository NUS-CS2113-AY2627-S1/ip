package sandy.task;

/**
 * Represents a task that starts and ends at specified times.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with a description, start time, and end time.
     *
     * @param description Text describing the event.
     * @param from Text describing when the event starts.
     * @param to Text describing when the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's start-time text.
     *
     * @return The event's start-time text.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event's end-time text.
     *
     * @return The event's end-time text.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns this event in the user-interface format.
     *
     * @return The task type, completion status, description, start time, and end time.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
