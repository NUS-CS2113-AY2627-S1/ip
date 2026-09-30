package xxlilchatxx;

/**
 * Represents a task that starts and ends at specific date/times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates a new event task with the given description, start, and end date/time.
     *
     * @param description Description of the task.
     * @param from Start date/time of the event.
     * @param to End date/time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + description + " | " + from + " | " + to;
    }
}
