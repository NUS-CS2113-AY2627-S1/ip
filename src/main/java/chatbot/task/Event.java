package chatbot.task;

/** Represents a task with a start time and an end time. */
public class Event extends Task {
    private final String from;
    private final String to;

    /** Creates a pending event task.
     *
     * @param description task description
     * @param from event start time
     * @param to event end time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the event start time for persistence.
     *
     * @return the event start time
     */
    public String getFrom() {
        return from;
    }

    /** Returns the event end time for persistence.
     *
     * @return the event end time
     */
    public String getTo() {
        return to;
    }

    /** Returns the event task type identifier.
     *
     * @return the event type identifier
     */
    @Override
    public String getTaskType() {
        return "E";
    }

    /** Returns the description together with the event times.
     *
     * @return formatted event text
     */
    @Override
    public String getDisplayText() {
        return super.getDisplayText() + " (from: " + from + " to: " + to + ")";
    }
}
