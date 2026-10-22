package slowbro;

/** Represents a task with a specified starting and ending date or time. */
public class Event extends Task {
    private static final String EVENT_PREFIX = "[E]";
    private static final String EVENT_FORMAT = " (from: %s to: %s)";

    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the event description
     * @param from the event's starting date or time
     * @param to the event's ending date or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the event start time. */
    public String getFrom() {
        return from;
    }

    /** Returns the event end time. */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return EVENT_PREFIX + super.toString()
                + String.format(EVENT_FORMAT, from, to);
    }
}
