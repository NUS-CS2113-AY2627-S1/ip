package yao.task;

/**
 * Represents a generic task with a description and completion status.
 * Serves as the superclass for specific task types such as Todo, Deadline, and Event.
 */
public class Task {
    /** Description of the task. */
    protected String description;
    /** Completion status flag of the task. */
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * Newly created tasks are incomplete by default.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
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
     * Returns an icon representing the completion status of the task.
     *
     * @return "X" if done, or " " if incomplete.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as incomplete.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the formatted string representation of this task for file storage.
     *
     * @return Formatted string for saving to disk.
     */
    public String toFileFormat() {
        return "TASK | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Checks if this task occurs on the specified date.
     *
     * @param date The date to check against.
     * @return True if the task occurs on the specified date, false otherwise.
     */
    public boolean isOnDate(java.time.LocalDate date) {
        return false;
    }
}
