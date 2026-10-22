package bond.task;

/**
 * Represents a task that occurs during a specified date or time period.
 */
public class Event extends Task {

    private final String from;
    private final String to;

    /**
     * Creates an event task that is initially not done.
     *
     * @param description Description of the task.
     * @param from Date or time when the event starts.
     * @param to Date or time when the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event task in the file storage format.
     *
     * @return Storage form of this event task.
     */
    @Override
    public String toDataString() {
        return "E" + DATA_FIELD_DELIMITER + super.toDataString()
                + DATA_FIELD_DELIMITER + escapeDataField(from)
                + DATA_FIELD_DELIMITER + escapeDataField(to);
    }

    /**
     * Returns the task type, status icon, description, and event period.
     *
     * @return Display form of this event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
