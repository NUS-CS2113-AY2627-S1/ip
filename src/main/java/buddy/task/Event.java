package buddy.task;

/**
 * Represents a task that occurs during a specific time span.
 */
public class Event extends Task {
    /** The date/time text at which this event starts, e.g. "Mon 2pm". */
    protected String from;

    /** The date/time text at which this event ends, e.g. "4pm". */
    protected String to;

    /**
     * Creates a new event with the given description and time span.
     *
     * @param description the text describing this event
     * @param from the date/time text at which this event starts
     * @param to the date/time text at which this event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }

    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + this.from + " | " + this.to;
    }
}
