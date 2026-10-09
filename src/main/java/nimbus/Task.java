package nimbus;

/**
 * Creates a task with the given description. The task starts off as not done.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description. The task starts off as not done.
     * @param description Description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Mark this task done.
     */

    public void markAsDone() {
        isDone = true;
    }

    /**
     * Mark this task as not done.
     */

    public void markAsNotDone() {
        isDone = false;
    }

    /**
     *  Returns the icon representing this task's status: "X" if done, a blank space otherwise.
     */

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the icon representing this task's type, e.g. "T" for a todo.
     */
    public abstract String getTypeIcon();

    /**
     * Returns the task description.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of this task, showing its status icon and description.
     */

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}