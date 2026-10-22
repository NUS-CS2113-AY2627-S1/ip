package dawn.task;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Represents a task's description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Constructs an incomplete task with the specified description.
     *
     * @param description the details of the task.
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Constructs a task with the specified description and completion status.
     *
     * @param description the details of the task.
     * @param isDone {@code true} if the task is already completed.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Retrieves the description of the task.
     *
     * @return the task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether the task is marked as completed.
     *
     * @return {@code true} if completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Updates the completion status of the task.
     *
     * @param done {@code true} to mark as completed, {@code false} to mark as pending.
     */
    public void setDone(boolean done) {
        isDone = done;
    }

    /**
     * Retrieves the status icon corresponding to the completion state.
     * X for completed, space for pending.
     *
     * @return the status string.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Checks if this task occurs on the specified calendar date.
     * By default, returns {@code false} for generic tasks.
     *
     * @param date the date to check against.
     * @return {@code true} if the task occurs on the given date, {@code false} otherwise.
     */
    public boolean isOnDate(LocalDate date) {
        return false;
    }

    /**
     * Checks if the task description contains the specified keyword (case-insensitive).
     *
     * @param keyword the substring keyword to look for.
     * @return {@code true} if the description contains the keyword, {@code false} otherwise.
     */
    public boolean containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }
        return description.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns the string representation of this task for UI display.
     *
     * @return the formatted task string.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }
}
