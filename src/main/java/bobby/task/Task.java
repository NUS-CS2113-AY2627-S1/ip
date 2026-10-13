package bobby.task;

/**
 * Represents a general task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description and incomplete status.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Creates a task with the given description and given status.
     *
     * @param description the description of the task
     * @param isDone the status of task's completion
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns an icon representing the task's completion status.
     *
     * @return "[X]" if the task is done, otherwise "[ ]"
     */
    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    /**
     * Returns the description of the task.
     *
     * @return the task description
     */
    public String getTaskDescription() {
        return description;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return true if the task is done, otherwise false
     */
    public boolean getStatus() {
        return isDone;
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as undone.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns a string representation of the task.
     *
     * @return the task's status icon followed by its description
     */
    public String toString() {
        return this.getStatusIcon() + " " + description;
    }
}
