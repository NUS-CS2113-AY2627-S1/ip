/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Returns the task description.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether this task is completed.
     *
     * @return true if this task is completed
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the character used to display the task status.
     *
     * @return X for a completed task, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the display form of this task.
     *
     * @return task status followed by its description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
