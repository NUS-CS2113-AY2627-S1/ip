package yuwei.task;

/**
 * A task that happens over a period of time, e.g. {@code project meeting} from
 * {@code Mon 2pm} to {@code 4pm}. The start and end are kept exactly as the user typed them.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Creates an event.
     *
     * @param description what the event is
     * @param from when the event starts, e.g. {@code Mon 2pm}
     * @param to when the event ends, e.g. {@code 4pm}
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getType() {
        return "[E]";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileString() {
        return "E | " + getDoneFlag() + " | " + description + " | " + from + " | " + to;
    }
}
