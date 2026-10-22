package vani.task;

/**
 * Represents an event task that has a description, a start date, and an end
 * date.
 */
public class Event extends Task {

    private String from;
    private String to;

    /**
     * Creates a new incomplete event task with the supplied description, start
     * date, and end date.
     *
     * @param description the text entered for the task
     * @param from the start date entered for the task
     * @param to the end date entered for the task
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's start date as the text supplied by the user.
     *
     * @return the start date text
     */
    public String getFrom() {
        return from;
    }

    /**
     * Replaces the start date text without changing the other event details.
     *
     * @param from the new start date text
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns the event's end date as the text supplied by the user.
     *
     * @return the end date text
     */
    public String getTo() {
        return to;
    }

    /**
     * Replaces the end date text without changing the other event details.
     *
     * @param to the new end date text
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Returns the event's type, completion status, description, start, and end for display.
     *
     * @return the task details prefixed with {@code [E]} and followed by the event's date range
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + getDescription() + " (from: " + from + " to: " + to + ")";
    }
}
