package cracker;

/**
 * Represents a task with a specified start and end time.
 */
public class Event extends Task {
    /** Text describing when the event starts. */
    private final String from;

    /** Text describing when the event ends. */
    private final String to;

    /**
     * Creates an incomplete event task with the given description and time range.
     *
     * @param description text describing the task
     * @param from text describing when the event starts
     * @param to text describing when the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start time text.
     *
     * @return the event start time text
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event end time text.
     *
     * @return the event end time text
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns this event task in the display format.
     *
     * @return this task's type, status, description, start time, and end time
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
