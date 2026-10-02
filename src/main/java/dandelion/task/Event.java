package dandelion.task;

/**
 * Represents a task that takes place during a specified period.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description Description of the task.
     * @param from Starting time of the event.
     * @param to Ending time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event's start time.
     *
     * @return Start time.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns this event's end time.
     *
     * @return End time.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns this event task in display-ready format.
     *
     * @return Display-ready event representation.
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] "
                + getDescription() + " (from: " + from + " to: " + to + ")";
    }
}
