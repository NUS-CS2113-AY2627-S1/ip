package goober.task;

/**
 * Represents a task that can be marked as done or not done.
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Returns the task description.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Creates a task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Checks whether the task is completed.
     *
     * @return {@code true} if completed.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the display marker for this task's completion state.
     *
     * @return {@code X} when the task is done, or a blank space otherwise.
     */
    public String getStatus() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the short identifier used when displaying this task.
     *
     * @return Task type identifier.
     */
    protected abstract String getTaskType();

    @Override
    public String toString() {
        return "[" + getTaskType() + "][" + getStatus() + "] " + description;
    }
}
