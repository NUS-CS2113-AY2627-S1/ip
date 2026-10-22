package dook;

/**
 * Represents a task that takes place during a specified time period.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Creates a new event task with the specified description and time period.
     *
     * @param description the description of the event task
     * @param from the starting date or time of the event
     * @param to the ending date or time of the event
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the starting date or time of this event.
     *
     * @return the event's starting date or time
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the ending date or time of this event.
     *
     * @return the event's ending date or time
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns a string representation of this event task.
     *
     * @return the event task with its type, completion status, and time period
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
