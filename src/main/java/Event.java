/**
 * A task that takes place over a period of time.
 */
public class Event extends Task {
    /** When the event starts, as typed by the user. */
    protected String from;
    /** When the event ends, as typed by the user. */
    protected String to;

    /**
     * Creates an event that is not done yet.
     *
     * @param from when the event starts, as typed by the user (e.g. "Mon 2pm").
     * @param to when the event ends, as typed by the user (e.g. "4pm").
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Formats the event as, e.g., "[E][ ] project meeting (from: Mon 2pm to: 4pm)". */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
