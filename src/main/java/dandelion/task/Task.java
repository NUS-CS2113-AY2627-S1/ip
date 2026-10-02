package dandelion.task;

/**
 * Represents a task that can be marked as done or not done.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Returns whether this task is completed.
     *
     * @return {@code true} if this task is completed.
     */
    public boolean isDone() {
        return isDone;
    }

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
     * Returns the icon that represents the task's completion status.
     *
     * @return {@code X} if the task is done, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the description of this task.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task in a display-ready format.
     *
     * @return Status icon followed by the task description.
     */
    @Override
    public abstract String toString();
}
