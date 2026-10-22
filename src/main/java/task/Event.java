package task;

/**
 * Represents an event task that starts and ends at specific date/time.
 */
public class Event extends Task{
    protected String from;
    protected String to;

    /**
     * Constructs a new Event task with the given description, start, and end.
     *
     * @param description The description of the event.
     * @param from The start date/time as a string.
     * @param to The end date/time as a string.
     */
    public Event(String description, String from, String to){
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date/time of this event.
     *
     * @return The start string.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the end date/time of this event.
     *
     * @return The end string.
     */
    public String getTo() {
        return to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description
                + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " +description + " | " +from + " | " + to;
    }
}
