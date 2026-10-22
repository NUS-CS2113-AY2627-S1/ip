package nga;

/**
 * Represents a task with a start date/time and an end date/time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an unfinished event with its description and date/time range.
     *
     * @param description the event description
     * @param from the event start date or time
     * @param to the event end date or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start date or time.
     *
     * @return the event start
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event end date or time.
     *
     * @return the event end
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the event details used to identify duplicate tasks.
     *
     * @return a key containing the task description and event range
     */
    @Override
    protected String getDuplicateKey() {
        return super.getDuplicateKey() + "|" + from + "|" + to;
    }

    /**
     * Returns the type label for an event.
     *
     * @return {@code "E"}, the storage and display label for events
     */
    @Override
    public String getTaskType() {
        return "E";
    }

    /**
     * Returns this event in the format used by the chatbot.
     *
     * @return the task description followed by its event range
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
