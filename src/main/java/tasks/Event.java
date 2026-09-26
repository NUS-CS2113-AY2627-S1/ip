package tasks;

/**
 * Represents a task spanning a start and end time, both retained as user-entered text.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Creates an incomplete event with a time range.
     *
     * @param description Event description.
     * @param from Start time text, such as Mon 2pm.
     * @param to End time text, such as 4pm.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the original start time text for display and storage.
     *
     * @return The event's start time text.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the original end time text for display and storage.
     *
     * @return The event's end time text.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the event type marker.
     *
     * @return The marker E.
     */
    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Adds the event's time range to its display description.
     *
     * @return The description followed by the start and end time text.
     */
    @Override
    public String getFullDescription() {
        return getDescription() + " (from: " + from + " to: " + to + ")";
    }
}
