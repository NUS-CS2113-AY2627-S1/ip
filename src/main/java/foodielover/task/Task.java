package foodielover.task;

import java.time.LocalDate;

/**
 * Represents a task with a description and a completion status.
 */
public class Task {
    /** Description of the task. */
    private final String description;

    /** Indicates whether the task is completed. */
    private boolean isDone;

    /**
     * Constructs a new Task with the specified description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon representing task completion.
     *
     * @return "X" if completed, otherwise " ".
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done yet.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the formatted string representation of the task for file storage.
     *
     * @return Formatted string for file storage.
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Checks if this task occurs on or is due on the specified date.
     * Subclasses with date information override this method.
     *
     * @param targetDate Date to compare against.
     * @return True if the task occurs on the specified date, false otherwise.
     */
    public boolean isOnDate(LocalDate targetDate) {
        return false;
    }

    /**
     * Checks if the task description contains the specified keyword (case-insensitive).
     *
     * @param keyword Keyword to search for.
     * @return True if the description contains the keyword, false otherwise.
     */
    public boolean containsKeyword(String keyword) {
        return description.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * Returns a string representation of the task with its status and description.
     *
     * @return Formatted task string.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
