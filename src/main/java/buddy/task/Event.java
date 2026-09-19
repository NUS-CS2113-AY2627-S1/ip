package buddy.task;

/**
 * Represents a task that happens during a specific period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description text that describes the task
     * @param from start date or time of the event
     * @param to end date or time of the event
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the icon that identifies this task type.
     *
     * @return event task icon
     */
    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns the display text for this event task.
     *
     * @return task status, description, start, and end
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event encoded for storage, with the start and end times
     * appended after the fields common to all tasks.
     *
     * @return this event encoded for storage
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + from + " | " + to;
    }
}
