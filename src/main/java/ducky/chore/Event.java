package ducky.chore;

/**
 * Represents an event: a chore that spans a period from a start time to an end time.
 */
public class Event extends Chore {

    /** Text describing when the event starts. */
    protected String from;

    /** Text describing when the event ends. */
    protected String to;

    /**
     * Creates an event with the given description and time span.
     *
     * @param description Text describing the chore.
     * @param from Text describing when the event starts.
     * @param to Text describing when the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns the description followed by the time span in parentheses.
     *
     * @return The chore description with its start and end times appended.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " (from: " + from + " to: " + to + ")";
    }
}
