package jamal.task;

/**
 * A task that starts at a specific date/time and end at a specific date/time,
 * e.g., (a) team project meeting 2/10/2019 2-4pm (b) orientation week 4/10/2019 to 11/10/2019.
 */
public class Event extends Task {
    /** Start time of the event, as typed by the user. */
    private String from;

    /** End time of the event, as typed by the user. */
    private String to;

    /**
     * Creates an event with the given description, start and end.
     *
     * @param description Text of the task.
     * @param from Start time, as typed by the user.
     * @param to End time, as typed by the user.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns when the event starts.
     *
     * @return Start time, as typed by the user.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns when the event ends.
     *
     * @return End time, as typed by the user.
     */
    public String getTo() {
        return to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
