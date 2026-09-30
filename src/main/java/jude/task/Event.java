package jude.task;

/**
 * Represents an event task, which is a task that starts at a specific time and ends at a specific time.
 */
public class Event extends Task {
    protected String start;
    protected String end;

    /**
     * Constructs an Event task with the specified description, start time, and end time.
     *
     * @param description The description of the event task.
     * @param start The starting date/time of the event.
     * @param end The ending date/time of the event.
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a string representation of the event task for displaying to the user.
     *
     * @return A string representation of the event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
    }

    /**
     * Returns a string representation of the event task formatted for saving to a file.
     *
     * @return A string representation of the event task for file storage.
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + start + " - " + end;
    }
}
