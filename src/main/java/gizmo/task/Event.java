package gizmo.task;

/**
 * A task that occurs between a specified start and end time.
 */
public class Event extends Task {

    protected String eventStart;
    protected String eventEnd;

    /** Creates an event task with its description, start time, and end time. */
    public Event(String description, String eventStart, String eventEnd) {
        super(description);
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    /** Returns the event start time. */
    public String getEventStart() {
        return eventStart;
    }

    /** Returns the event end time. */
    public String getEventEnd() {
        return eventEnd;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + eventStart + " to: " + eventEnd + ")";
    }
}
