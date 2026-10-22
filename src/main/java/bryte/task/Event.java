package bryte.task;

import java.time.LocalDateTime;

import bryte.exception.BryteException;
import bryte.util.DateTimeUtil;

/**
 * Represents a task that starts at a specific date/time and ends at a specific date/time.
 */
public class Event extends Task {

    /** The parsed start date and time of the event, or null if unparseable. */
    protected LocalDateTime startTime;
    /** The parsed end date and time of the event, or null if unparseable. */
    protected LocalDateTime endTime;
    /** The raw start time string provided by the user or file. */
    protected String rawStartTime;
    /** The raw end time string provided by the user or file. */
    protected String rawEndTime;

    /**
     * Constructs a new {@code Event} task with the specified description, start time, and end time.
     *
     * @param description The description or name of the task.
     * @param startTimeString The start time of the event.
     * @param endTimeString The end time of the event.
     */
    public Event(String description, String startTimeString, String endTimeString) {
        super(description, false);
        this.rawStartTime = startTimeString.trim();
        this.rawEndTime = endTimeString.trim();
        try {
            this.startTime = DateTimeUtil.parse(startTimeString);
            this.endTime = DateTimeUtil.parse(endTimeString);
        } catch (BryteException e) {
            this.startTime = null;
            this.endTime = null;
        }
    }

    /**
     * Constructs a new {@code Event} task with parsed LocalDateTimes.
     * Used for new tasks where date format is strictly enforced.
     *
     * @param description The description or name of the task.
     * @param startTime The parsed start time.
     * @param endTime The parsed end time.
     */
    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description, false);
        this.startTime = startTime;
        this.endTime = endTime;
        this.rawStartTime = DateTimeUtil.formatForStorage(startTime);
        this.rawEndTime = DateTimeUtil.formatForStorage(endTime);
    }

    /**
     * Returns the string representation of the Event task.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        if (startTime != null && endTime != null) {
            return "[E]" + super.toString() + " (from: " + DateTimeUtil.formatForDisplay(startTime)
                    + " to: " + DateTimeUtil.formatForDisplay(endTime) + ")";
        }
        return "[E]" + super.toString() + " (from: " + rawStartTime + " to: " + rawEndTime + ")";
    }

    /**
     * Returns the string representation of the Event task for file storage.
     *
     * @return The formatted string for saving to a file.
     */
    @Override
    public String toFileFormat() {
        if (startTime != null && endTime != null) {
            return "E | " + super.toFileFormat() + " | " + DateTimeUtil.formatForStorage(startTime)
                    + " | " + DateTimeUtil.formatForStorage(endTime);
        }
        return "E | " + super.toFileFormat() + " | " + rawStartTime + " | " + rawEndTime;
    }

    /**
     * Checks if the event occurs on the specified date.
     * Checks both the start and end dates.
     *
     * @param date The date to check.
     * @return {@code true} if the event starts or ends on the date, {@code false} otherwise.
     */
    @Override
    public boolean isOnDate(java.time.LocalDate date) {
        if (startTime != null && endTime != null) {
            return startTime.toLocalDate().isEqual(date) || endTime.toLocalDate().isEqual(date);
        }
        return false;
    }
}
