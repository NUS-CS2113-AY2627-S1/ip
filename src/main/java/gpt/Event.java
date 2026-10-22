package gpt;

/**
 * Represents a task that starts and ends at given dates or times.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Creates an incomplete event with the given description, start, and end.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
