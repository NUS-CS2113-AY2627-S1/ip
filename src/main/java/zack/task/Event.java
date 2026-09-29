package zack.task;

/** Represents an event with start and end values stored as text. */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an initially incomplete event with textual start and end values.
     *
     * @param description description of the event
     * @param from start date or time as text
     * @param to end date or time as text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's start value without converting it to a date.
     *
     * @return start date or time as text
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event's end value without converting it to a date.
     *
     * @return end date or time as text
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the text used to display this task.
     *
     * @return description with completion status and any type-specific details
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from + " to: " + to + ")";
    }
}