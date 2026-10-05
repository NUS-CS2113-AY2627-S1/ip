package ernest.task;

/**
 * Represents a task that occurs within a date-time range.
 */
public class Event extends Task {
    private static final String ERROR_END_BEFORE_START = "Event end cannot be before its start.";

    /** Start of the event. */
    private final TaskDateTime startDateTime;
    /** End of the event. */
    private final TaskDateTime endDateTime;

    /**
     * Creates an event task.
     *
     * @param description description of the task.
     * @param startDateTime start date and optional time of the event.
     * @param endDateTime end date and optional time of the event.
     * @throws IllegalArgumentException if the end occurs before the start.
     */
    public Event(String description, TaskDateTime startDateTime, TaskDateTime endDateTime) {
        super(description);
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException(ERROR_END_BEFORE_START);
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Returns this event's start date and optional time.
     *
     * @return start date and optional time.
     */
    public TaskDateTime getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * Returns this event's end date and optional time.
     *
     * @return end date and optional time.
     */
    public TaskDateTime getEndDateTime() {
        return this.endDateTime;
    }

    /**
     * Returns this task formatted with its event period.
     *
     * @return display representation of this event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startDateTime + " to: " + endDateTime + ")";
    }
}
