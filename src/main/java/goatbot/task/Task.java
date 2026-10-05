package goatbot.task;

import java.time.LocalDate;

/**
 * Represents one task in the task list.
 */
public class Task {
    private static final String DONE_STATUS_ICON = "X";
    private static final String NOT_DONE_STATUS_ICON = " ";

    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description text that describes the task
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Returns the icon representing this task's completion status.
     *
     * @return completion status icon
     */
    public String getStatusIcon() {
        return isDone ? DONE_STATUS_ICON : NOT_DONE_STATUS_ICON;
    }

    /**
     * Returns the task description prefixed by its completion status.
     *
     * @return display representation of this task
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
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
     * Returns the description of this task.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns this task in the pipe-separated format used for storage.
     *
     * @return stored representation of this task
     */
    public String toFileString() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns whether this task occurs on the specified date.
     *
     * @param date date to check
     * @return true if this task occurs on the date
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }
}
