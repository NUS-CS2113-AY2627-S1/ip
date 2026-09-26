package moistbot.task;

import moistbot.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a task with a specific deadline.
 * Extends Task to add a deadline field, displayed when the task is converted to a string.
 */
public class Deadline extends Task {
    private final LocalDate deadlineDate;
    private final LocalTime deadlineTime;

    /**
     * Constructs a Deadline task with the given description and deadline.
     *
     * @param description The description of the deadline task
     * @param deadlineDate The date by which the task should be completed
     * @param deadlineTime The optional time by which the task should be completed
     */
    public Deadline(String description, LocalDate deadlineDate, LocalTime deadlineTime) {
        super(description, TYPE_DEADLINE);
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

    /**
     * Returns the deadline date.
     *
     * @return The deadline date
     */
    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    /**
     * Returns the optional deadline time.
     *
     * @return The deadline time, or {@code null} when no time was supplied
     */
    public LocalTime getDeadlineTime() {
        return deadlineTime;
    }

    /**
     * Returns whether this deadline is due by the inclusive cutoff date.
     *
     * @param cutoffDate The inclusive date limit
     * @return True if the deadline is due on or before the cutoff
     */
    @Override
    public boolean isDatedOnOrBefore(LocalDate cutoffDate) {
        return !deadlineDate.isAfter(cutoffDate);
    }

    /**
     * Returns a string representation of the deadline task.
     * Appends the deadline in the format {@code (by: <deadline>)} to the base task description.
     *
     * @return The formatted string with task description and deadline
     */
    @Override
    public String toString() {
        return super.toString() + " (by: "
                + DateTimeUtil.formatForDisplay(deadlineDate, deadlineTime) + ")";
    }
}
