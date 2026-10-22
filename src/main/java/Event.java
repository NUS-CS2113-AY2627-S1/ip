/**
 * Represents a task that takes place during a specified time period.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event task.
     *
     * @param description description of the event
     * @param from start date or time
     * @param to end date or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a displayable representation of this event.
     *
     * @return formatted event text
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
