package dash.task;

import java.time.LocalDate;

/**
 * Represents one task and its completion state.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description The text describing the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns whether this task is marked as done.
     *
     * @return {@code true} if completed; otherwise {@code false}.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the icon used to show this task's completion state.
     *
     * @return {@code X} when complete; otherwise a space.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Checks whether this task occurs on the specified date.
     *
     * @param date The date to check against.
     * @return {@code true} if the task occurs on the specified date; {@code false} otherwise.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Checks whether this task's description contains the specified search keyword.
     *
     * @param keyword The keyword to search for.
     * @return {@code true} if the description contains the keyword; {@code false} otherwise.
     */
    public boolean containsKeyword(String keyword) {
        return description.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * Returns the formatted string representation of this task for file storage.
     *
     * @return The string representation of this task to save to the hard disk.
     */
    public abstract String toFileFormat();

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
