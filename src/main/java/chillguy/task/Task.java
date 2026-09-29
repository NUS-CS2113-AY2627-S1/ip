package chillguy.task;

/**
 * Represents a task with a description and done status.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task that is not done yet.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task description used for display, searching, and storage.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been marked as done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon shown in task lists.
     *
     * @return {@code X} if the task is done, or a blank space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
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
     * Returns the icon representing this task's type.
     *
     * @return Task type icon.
     */
    public abstract String getTaskTypeIcon();

    /**
     * Returns the task's type and status icons followed by its description for console display.
     */
    @Override
    public String toString() {
        return "[" + getTaskTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
