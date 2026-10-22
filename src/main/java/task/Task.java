package task;

/**
 * Represents an abstract task with a description and completion status.
 * Subclasses must define the task type icon and file storage format.
 */
public abstract class Task {

    protected String description;
    protected boolean isDone;

    /**
     * Constructs a new task with the given description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "X" if the task is done, otherwise a space.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
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
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task is done.
     *
     * @return true if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the type icon of the task.
     * Subclasses must implement this method.
     *
     * @return The type icon (e.g., "T", "D", "E").
     */
    public abstract String getTypeIcon();

    /**
     * Returns the task in file storage format.
     * Subclasses must implement this method.
     *
     * @return A string representation for file storage.
     */
    public abstract String toFileFormat();

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
