package dobby.task;

/**
 * Represents a task in Dobby's task list.
 *
 * <p>A plain {@code Task} is treated as a ToDo for backwards compatibility
 * with the original command interface. Specialized task types override
 * {@link #toString()} to add their own display details.
 */
public class Task {

    /** The task's description. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /**
     * Creates a new unfinished task.
     *
     * @param description the task's description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol used to display this task's completion status.
     *
     * @return {@code X} for a completed task or a space for an unfinished task
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns this task in the compact format used by the user interface.
     *
     * @return the ToDo type icon, completion icon, and description
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Returns this task to the unfinished state. */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Returns the text describing this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return whether the task is done.
     */
    public boolean isDone() {
        return isDone;
    }
}
