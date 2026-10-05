package hedy.task;

/**
 * A task that happens between a start time and an end time.
 */
public class Event extends Task {
    protected String start;
    protected String end;

    /**
     * Creates an event.
     *
     * @param description what the event is
     * @param start when it starts, stored as the user typed it
     * @param end when it ends, stored as the user typed it
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + start + " | " + end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
    }
}
