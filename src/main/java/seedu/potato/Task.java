package seedu.potato;

import java.time.LocalDate;

/**
 * Represents a task and its completion state.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the icon used to display this task's completion state.
     *
     * @return {@code "X"} if done; otherwise a space.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Reports whether this task's description contains the given search text.
     *
     * @param searchText Text to find in the description.
     * @return {@code true} if the description contains the search text.
     */
    public boolean containsDescription(String searchText) {
        return description.contains(searchText);
    }

    /**
     * Reports whether this task stores the given date.
     *
     * @param date Date to find in this task.
     * @return {@code true} if the task stores the date.
     */
    public boolean containsDate(LocalDate date) {
        return false;
    }

    /**
     * Returns the numeric completion state used in the save file.
     *
     * @return {@code "1"} if done; otherwise {@code "0"}.
     */
    protected String getDataStatus() {
        return isDone ? "1" : "0";
    }

    /**
     * Returns this task in the format used for persistent storage.
     *
     * @return Save-file representation of this task.
     */
    public abstract String toDataString();

    /**
     * Returns the task description with its completion icon.
     *
     * @return Display-ready task text.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
