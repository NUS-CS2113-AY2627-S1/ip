package mimimeow.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Represents a task that takes place during a specified time period. */
public class Event extends Task implements ScheduledTask {
    private final TaskDateTime startTime;
    private final TaskDateTime endTime;

    /**
     * Creates an event with the specified description, start time, and end time.
     *
     * @param description description of the event
     * @param startTime time when the event starts
     * @param endTime time when the event ends
     */
    public Event(String description, String startTime, String endTime) {
        super(description);
        if (startTime == null || startTime.isBlank() || endTime == null || endTime.isBlank()) {
            throw new InvalidTaskException("An event needs both start and end times.");
        }
        this.startTime = new TaskDateTime(startTime);
        this.endTime = new TaskDateTime(endTime);
        if (this.endTime.isBefore(this.startTime)) {
            throw new InvalidTaskException("An event's end time cannot be before its start time.");
        }
    }

    /** Returns this event in the storage format. */
    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | "
                + escapeFileField(startTime.toFileString()) + " | "
                + escapeFileField(endTime.toFileString());
    }

    /** Returns whether this event overlaps the specified date. */
    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(startTime.toLocalDate()) && !date.isAfter(endTime.toLocalDate());
    }

    /** Returns whether this event starts at or after the specified date and time. */
    @Override
    public boolean isUpcomingAt(LocalDateTime dateTime) {
        return !startTime.isBefore(dateTime);
    }

    /** Returns false because events are not treated as overdue tasks. */
    @Override
    public boolean isOverdueAt(LocalDateTime dateTime) {
        return false;
    }

    /** Returns the event start time used for chronological ordering. */
    @Override
    public TaskDateTime getScheduleTime() {
        return startTime;
    }

    /** Returns this event in the user-facing display format. */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + startTime + " to: " + endTime + ")";
    }
}
