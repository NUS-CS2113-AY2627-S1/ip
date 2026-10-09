package friday;

/**
 * A task that happens over a period of time. The start and end are kept as
 * free text (e.g. "Mon 2pm") rather than parsed dates.
 */
public class Event extends Task {

    protected String from;
    protected String to;

    /**
     * Creates an event that is initially not done.
     *
     * @param description what the event is
     * @param from        when it starts, as free text
     * @param to          when it ends, as free text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the display form, e.g. {@code [E][ ] meeting (from: Mon 2pm to: Mon 4pm)}. */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] "
                + description
                + " (from: " + from
                + " to: " + to + ")";
    }

    /** Returns the save-file line, e.g. {@code E | 0 | meeting | Mon 2pm | Mon 4pm}. */
    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }
}