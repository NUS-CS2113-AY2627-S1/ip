package anson;

/**
 * Represents a task that spans a specific time period, from a start to an end.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event task with the given description, start, and end.
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

    /**
     * Returns the string representation of this event task.
     *
     * @return Formatted string in the form "[E][status] description (from: ... to: ...)".
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns the save-file line for this event task.
     *
     * @return String in the form "E | isDone | description | from | to".
     */
    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from + " | " + to;
    }
}