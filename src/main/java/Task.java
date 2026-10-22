/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns this task's description.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the display icon representing this task's completion status.
     *
     * @return {@code X} for completed tasks, otherwise a blank space
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Updates this task's completion status.
     *
     * @param isDone whether the task is complete
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns a displayable representation of this task.
     *
     * @return formatted task text
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
