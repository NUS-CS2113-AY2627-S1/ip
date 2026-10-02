package nova.task;
/**
 * Represents a task that spans a period of time, with a start and end.
 */

public class Event extends Task {

    protected String from;
    protected String to;

    /**
     * Creates a new Event with the given description, start, and end.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the type icon for an Event task.
     */
    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns the display representation of this event, including its time range.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event as a save-file line, including its time range.
     */
    @Override
    public String toSaveString() {
        return super.toSaveString() + " | " + from + " | " + to;
    }
}