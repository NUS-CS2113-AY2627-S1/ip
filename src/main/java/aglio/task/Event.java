package aglio.task;

/**
 * Represents a task that spans a time period, with a start and end time.
 */
public class Event extends Task {

    protected String from;
    protected String to;

    /**
     * Creates an Event task with the given description, start time, and end time.
     *
     * @param description what the event is about
     * @param from when the event starts
     * @param to when the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
