package dawn.task;

import java.time.LocalDate;

/**
 * Subclass Event: A task with a description, start date, and end date.
 */
public class Event extends Task {
    private final TaskDateTime startDate;
    private final TaskDateTime endDate;

    /**
     * Constructs a new Event task with the specified description, start date, and end date.
     *
     * @param description the description of the event.
     * @param startDate the start date of the event.
     * @param endDate the end date of the event.
     */
    public Event(String description, TaskDateTime startDate, TaskDateTime endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructs an Event task with a specific completion status.
     *
     * @param description the description of the event.
     * @param startDate the start date of the event.
     * @param endDate the end date of the event.
     * @param isDone {@code true} if the event is already completed.
     */
    public Event(String description, TaskDateTime startDate, TaskDateTime endDate, boolean isDone) {
        super(description, isDone);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Retrieves the start date of this event.
     *
     * @return the start date value.
     */
    public TaskDateTime getStartDate() {
        return startDate;
    }

    /**
     * Retrieves the end date of this event.
     *
     * @return the end date value.
     */
    public TaskDateTime getEndDate() {
        return endDate;
    }

    /**
     * Checks if this event is running on the specified calendar date.
     *
     * @param date the date to check against.
     * @return {@code true} if the event spans across or starts on the given date, {@code false} otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate date) {
        if (startDate.hasDate() && endDate.hasDate()) {
            LocalDate start = startDate.toLocalDate();
            LocalDate end = endDate.toLocalDate();
            return !date.isBefore(start) && !date.isAfter(end);
        }
        if (startDate.hasDate()) {
            return startDate.toLocalDate().equals(date);
        }
        return false;
    }

    /**
     * Returns the string representation of this event for UI display.
     *
     * @return the formatted event string.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startDate.toDisplayString()
                + " to: " + endDate.toDisplayString() + ")";
    }
}
