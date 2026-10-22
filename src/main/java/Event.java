/**
 * Represents a task that occurs during a specified time range.
 */
public class Event extends Task {

    /** The event start time entered by the user. */
    protected String from;
    /** The event end time entered by the user. */
    protected String to;

    /**
     * Creates an event task with a description, start time, and end time.
     *
     * @param description the task description
     * @param from the event start time
     * @param to the event end time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Formats the event task for display.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Formats the event task for persistent storage.
     *
     * @return the encoded event task
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }
}
