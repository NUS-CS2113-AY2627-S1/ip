package dexter.tasks;

/** Represents a basic task with a description and completion status. */
public class Task {
    protected String description;
    protected String type;
    protected boolean isDone;

    /** Creates an unfinished basic task with the given description. */
    public Task(String description) {
        this.description = description;
        this.type = "T";
        this.isDone = false;
    }

    /**
     * Returns the symbol used to show whether this task is complete.
     *
     * @return {@code "X"} when complete, or a blank space otherwise
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the storage type code for this task.
     *
     * @return {@code "T"} for a basic task
     */
    public String getType() {
        return type;
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Formats this task for display in the user interface.
     *
     * @return the task type, status, and description in display format
     */
    @Override
    public String toString() {
        return "[" + type + "]" + "[" + this.getStatusIcon() + "] " + description;
    }
}
