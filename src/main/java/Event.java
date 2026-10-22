/**
 * Represents a task with a start and end time.
 */
public class Event extends Task {
    private String startTime;
    private String endTime;

    /**
     * Constructs an event task with the given description, start time, and end time.
     *
     * @param description description of the task.
     * @param startTime start time of the event.
     * @param endTime end time of the event.
     */
    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "[E]" + getStatusIcon()
                + getDescription()
                + " (from: " + startTime + " to: " + endTime + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + startTime + " | " + endTime;
    }
}
