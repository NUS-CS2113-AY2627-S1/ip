package doug.task;

/**
 * Represents a task that takes place between a start and end date or time.
 */
public class Event extends Task {
    /** Start date or time supplied for the event. */
    private String from;
    /** End date or time supplied for the event. */
    private String to;

    /**
     * Creates an incomplete event.
     *
     * @param description description of the event
     * @param from start date or time of the event
     * @param to end date or time of the event
     */
    public Event(String description, String from, String to) {
        super(Type.EVENT, description);
        this.from = from;
        this.to = to;
    }

    /**
     * Reconstructs an event with an existing completion state.
     *
     * @param description description of the event
     * @param from start date or time of the event
     * @param to end date or time of the event
     * @param isDone whether the event has been completed
     */
    public Event(String description, String from, String to, boolean isDone) {
        super(Type.EVENT, isDone, description);
        this.from = from;
        this.to = to;
    }

    /**
     * Converts this event to its pipe-delimited storage representation.
     *
     * @return serialized event containing its type, completion state, description,
     *         start value, and end value
     */
    @Override
    public String toSaveFormat() {
        return String.format("E|%d|%s|%s|%s", isDone ? 1 : 0, description, from, to);
    }

    /**
     * Returns a display representation that includes the event's date range.
     *
     * @return formatted event for display to the user
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
