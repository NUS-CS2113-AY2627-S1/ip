package alfred.task;

import java.time.LocalDate;

/**
 * Represents a task and its completion status.
 */
public class Task {

    private final String description;
    private boolean isDone;

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
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the icon representing the task's completion status.
     *
     * @return {@code X} if completed, or a space if not completed.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task fields in the format used by the data file.
     *
     * @return Completion status and description separated by a pipe.
     */
    public String toDataString() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns whether this task occurs on the specified date.
     *
     * @param date Date to check.
     * @return True if this task occurs on the date.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
