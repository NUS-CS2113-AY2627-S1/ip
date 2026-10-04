package dobby.task;

import java.time.LocalDate;

/**
 * Represents a task with a specified start and end date or time.
 */
public class Event extends Task {

    /** The date or time when this event starts. */
    private final String start;

    /** The date or time when this event ends. */
    private final String end;

    /**
     * Creates an unfinished event.
     *
     * @param description the event's description.
     * @param start the date or time when the event starts.
     * @param end the date or time when the event ends.
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the date or time when this event starts.
     *
     * @return the event start text.
     */
    public String getStart() {
        return start;
    }

    /**
     * Returns the date or time when this event ends.
     *
     * @return the event end text.
     */
    public String getEnd() {
        return end;
    }

    /**
     * Returns the parsed start date when the event starts on a recognized date.
     *
     * @return the event start date, or {@code null} when the start is free-form text.
     */
    public LocalDate getStartDate() {
        return DateTimeParser.parseDate(start);
    }

    /**
     * Returns the parsed end date when the event ends on a recognized date.
     *
     * @return the event end date, or {@code null} when the end is free-form text.
     */
    public LocalDate getEndDate() {
        return DateTimeParser.parseDate(end);
    }

    /**
     * Returns this event in the compact format used by the user interface.
     *
     * @return the event type icon, completion icon, description, and date range.
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + start + " to: " + end + ")";
    }
}
