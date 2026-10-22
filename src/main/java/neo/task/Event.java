package neo.task;

/**
 * Represents a task that starts at a specific time and ends at a specific time.
 * Inherits from the base Task class and adds start and end time attributes.
 */
public class Event extends Task {
    /** The start date and time of the event. */
    private String from;

    /** The end date and time of the event. */
    private String to;

    /**
     * Creates an incomplete event task with the specified description, start time, and end time.
     *
     * @param description Text that describes the event.
     * @param from The starting date and time of the event.
     * @param to The ending date and time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a string representation of this event task for UI display.
     *
     * @return A formatted string including the task type, status, description, and event timeframe.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns a string representation of this event task for saving to a persistent file.
     *
     * @return A pipe-separated record with escaped, readable text fields.
     */
    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + escapeForStorage(from)
                + " | " + escapeForStorage(to);
    }
}
