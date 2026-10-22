package cole;

/**
 * Represents a task that happens over a period of time, with a start and an end.
 */
public class Event extends Task {

    private String from;
    private String to;

    /**
     * Creates an event with the given description, start and end.
     *
     * @param description what the event is
     * @param from when the event starts, e.g. "Mon 2pm"
     * @param to when the event ends, e.g. "4pm"
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event as shown to the user,
     * e.g. "[E][ ] meeting (from: Mon 2pm to: 4pm)".
     *
     * @return the event with its start and end
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns the event in data file format,
     * e.g. "E | 0 | meeting | Mon 2pm | 4pm".
     *
     * @return one line of the data file
     */
    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from + " | " + to;
    }

}
