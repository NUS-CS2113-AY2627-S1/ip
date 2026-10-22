package task;

/**
 * Represents a task scheduled between a start and end time.
 */
public class Event extends Task {
    private final String by;
    private final String from;

    /**
     * Creates an event task with its description and time range.
     *
     * @param description Description of the event task.
     * @param from Start time of the event.
     * @param by End time of the event.
     */
    public Event(String description, String from, String by) {
        super(description, TaskType.EVENT);
        this.by = by;
        this.from = from;
    }

    /** Returns this event in the format used for persistent storage. */
    /**
     * Returns this event in the format used for persistent storage.
     *
     * @return Serialized event data.
     */
    @Override
    public String toFileString() {
        return super.toFileString() + " | " + escapeFileField(from) + " | " + escapeFileField(by);
    }

    /** Returns a display-friendly representation of this event. */
    /**
     * Returns a display-friendly representation of this event.
     *
     * @return Display text for this event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + by + ")";
    }
}
