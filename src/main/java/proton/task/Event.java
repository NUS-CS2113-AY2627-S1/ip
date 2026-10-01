package proton.task;

/**
 * Represents a task that occurs between specific start and end date/times.
 */
public class Event extends Task {
    private final String startDateTime;
    private final String endDateTime;

    /**
     * Creates an event task that is initially not completed.
     *
     * @param description Description of the event task.
     * @param startDateTime Date or time at which the event starts.
     * @param endDateTime Date or time at which the event ends.
     */
    public Event(String description, String startDateTime, String endDateTime) {
        super(description);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Returns this task with its event type marker and start and end date/times.
     *
     * @return The formatted event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + startDateTime + " to: " + endDateTime + ")";
    }

    /**
     * Returns the date or time at which this event starts.
     *
     * @return The event's start date or time.
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * Returns the date or time at which this event ends.
     *
     * @return The event's end date or time.
     */
    public String getEndDateTime() {
        return endDateTime;
    }
}
