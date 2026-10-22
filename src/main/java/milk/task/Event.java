package milk.task;

/**
 * Represents a task with "from" and "to" values.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructor of the Event task.
     * @param description The Event's description.
     * @param from The Event's start datetime.
     * @param to The Event's end datetime.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the start datetime of the Event
     * @return The start datetime of the Event
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the start datetime of the Event
     * @param from The start datetime of the Event
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the end datetime of the Event
     * @return The end datetime of the Event
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the end datetime of the Event
     * @param to The end datetime of the Event
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Getter of the Event task icon.
     * Overrides the parent class's function.
     * @return Event task icon.
     */
    @Override
    public String getTaskIcon() {
        return "[E]";
    }

    /**
     * Overrides the toString function.
     * @return The task icon, followed by the status icon, followed by the description, followed by the start and end datetimes.
     */
    @Override
    public String toString() {
        return getTaskIcon() + getStatusIcon() + " " + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }
}
