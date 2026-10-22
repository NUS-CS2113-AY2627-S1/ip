package bob.task;

/**
 * Represents a task with start and end times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event with the given description, start time, and end time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start time.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the event start time to the supplied text.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns the event end time.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the event end time to the supplied text.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Returns the event marker, completion status, description, and start and end times.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
