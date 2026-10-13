package bobby.task;

/**
 * Represents a task with a start and end time
 */
public class Event extends Task {
    private final String start;
    private final String end;

    /**
     * Creates an Event task with incomplete status.
     *
     * @param description the description of the task
     * @param start the start time of the event
     * @param end the end time of the event
     */
    public Event(String description, String start, String end) {
        this(description, false, start, end);
    }

    /**
     * Creates an Event task with a given status.
     *
     * @param description the description of the task
     * @param isDone the status of task's completion
     * @param start the start time of the event
     * @param end the end time of the event
     */
    public Event(String description, boolean isDone, String start, String end) {
        super(description, isDone);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the start time of this event.
     *
     * @return the start time of the event
     */
    public String getStart() {
        return start;
    }

    /**
     * Returns the end time of this event.
     *
     * @return the end time of the event
     */
    public String getEnd() {
        return end;
    }

    /**
     * Returns a string representation of this Event task.
     *
     * @return a string containing the task description and event timeframe
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " -> Start: {" + start + "} ~ End: {" + end + "}";
    }
}