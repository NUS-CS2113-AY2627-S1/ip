package bill.task;

/**
 * Represents a task that takes place between a starting and ending time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with its description and timing details.
     *
     * @param description Description of the event.
     * @param from Starting date or time as entered by the user.
     * @param to Ending date or time as entered by the user.
     */
    public Event(String description, String from, String to) {
        this(description, from, to, false);
    }

    /**
     * Creates an event with its description, timing details, and completion state.
     *
     * @param description Description of the event.
     * @param from Starting date or time as entered by the user.
     * @param to Ending date or time as entered by the user.
     * @param isDone Whether the event is completed.
     */
    public Event(String description, String from, String to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's starting date or time.
     *
     * @return Starting date or time as entered by the user.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event's ending date or time.
     *
     * @return Ending date or time as entered by the user.
     */
    public String getTo() {
        return to;
    }

    /**
     * Identifies this task as an event in task listings.
     *
     * @return The event type icon.
     */
    @Override
    protected String getTypeIcon() {
        return "E";
    }

    /**
     * Includes the event's starting and ending times in its display text.
     *
     * @return The time-range suffix shown after the description.
     */
    @Override
    protected String getDetails() {
        return " (from: " + from + " to: " + to + ")";
    }
}
