package moistbot.task;

import moistbot.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a task that occurs during a specific time period.
 * Extends Task to add start and end time fields, displayed when the task is converted to a string.
 */
public class Event extends Task {
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final LocalDate endDate;
    private final LocalTime endTime;

    /**
     * Constructs an Event task with the given description, start time, and end time.
     *
     * @param description The description of the event
     * @param startDate The event start date
     * @param startTime The optional event start time
     * @param endDate The event end date
     * @param endTime The optional event end time
     */
    public Event(String description, LocalDate startDate, LocalTime startTime,
            LocalDate endDate, LocalTime endTime) {
        super(description, TYPE_EVENT);
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    /**
     * Returns the event start date.
     *
     * @return The event start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the optional event start time.
     *
     * @return The start time, or {@code null} for a date-only event
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the event end date.
     *
     * @return The event end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the optional event end time.
     *
     * @return The end time, or {@code null} for a date-only event
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns whether this event starts by the inclusive cutoff date.
     *
     * @param cutoffDate The inclusive date limit
     * @return True if the event starts on or before the cutoff
     */
    @Override
    public boolean isDatedOnOrBefore(LocalDate cutoffDate) {
        return !startDate.isAfter(cutoffDate);
    }

    /**
     * Returns a string representation of the event task.
     * Appends the time period in the format {@code (from: <start> to: <end>)} to the task description.
     *
     * @return The formatted string with task description and time period
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + DateTimeUtil.formatForDisplay(startDate, startTime)
                + " to: " + DateTimeUtil.formatForDisplay(endDate, endTime) + ")";
    }
}
