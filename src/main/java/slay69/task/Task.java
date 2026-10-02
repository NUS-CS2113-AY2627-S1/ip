package slay69.task;

/**
 * Represents a task with a description and a completion status.
 */
public class Task {
    /** Description displayed to the user and written to storage. */
    protected String description;

    /** Whether this task has been marked as completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task description used for keyword searches.
     *
     * @return the unformatted task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns X for a completed task or a space for an incomplete task.
     *
     * @return the one-character status icon
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the status and description in the saved-file format.
     * Subclasses prefix a task type and append their extra fields.
     *
     * @return the serialized task fields
     */
    public String toStorageString() {
        return (isDone ? "1" : "0") + "|" + description;
    }

    /**
     * Returns the status and description for display to the user.
     *
     * @return the displayed task text
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
