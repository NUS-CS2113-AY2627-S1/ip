package duke;

/**
 * Represents a task that starts and ends at specific date/times.
 */
public class Event extends Task {

    protected String from;
    protected String to;

    /**
     * Creates a new event task.
     *
     * @param description What the task is about.
     * @param from        When the event starts.
     * @param to          When the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}