package dook.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import dook.io.SerialisedData;

/**
 * Represents the template for all tasks.
 */
public abstract class Task implements Comparable<Task> {
    private static final String TIME_SLOT_PATTERN = "HH:mm";

    protected final String paddingForTimeSlot = " ".repeat(TIME_SLOT_PATTERN.length() + 1);
    protected final DateTimeFormatter TimeSlotFormatter =
        DateTimeFormatter.ofPattern(TIME_SLOT_PATTERN, Locale.UK);

    private String description;
    private boolean isDone;

    /**
     * Represents the template for constructors for all tasks
     * with the specified description.
     * Marks the new task as not done.
     *
     * @param  description The description of the task.
     */
    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     *
     * @return The description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the status of the task (done or not).
     *
     * @return The current status of the task.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Modify the status of the task (done or not).
     *
     * @param isDone The desired status of the task.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns whether the task occurs on the given date.
     *
     * @param  date The date to check.
     * @return      Whether the task occurs on the date.
     */
    public boolean isOnDate(LocalDate date) {
        return false;
    }

    /**
     * Returns whether the task is expired.
     *
     * @return Whether the task is expired.
     */
    public boolean isExpired() {
        return false;
    }

    /**
     * Return the serialised data of the task.
     *
     * @return The task in list of strings format.
     */
    public SerialisedData serialise() {
        return new SerialisedData(
            getClass(),
            isDone,
            description
        );
    }

    /**
     * Returns the value used to sort this task.
     *
     * @return The sort value.
     */
    protected abstract LocalDateTime getSortValue();

    /**
     * Returns a string representation including the task's time slot.
     *
     * @return The string representation with time.
     */
    public abstract String toStringWithTime();

    /**
     * Compares this task with another task by sort value.
     *
     * @param  other The other task to compare with.
     * @return       The comparison result.
     */
    @Override
    public int compareTo(Task other) {
        return this.getSortValue().compareTo(other.getSortValue());
    }

    /**
     * Returns pretty-print of the task,
     * includes task type, task status (done or not), and task description.
     *
     * @return The task in string format.
     */
    @Override
    public String toString() {
        return "[%s] %s".formatted(isDone ? "X" : " ", description);
    }
}
