package zack.task;

/** Represents a task with a description and a completion status. */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an initially incomplete task.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol used to display completion status.
     *
     * @return "X" if completed, or a single space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task description without display formatting.
     *
     * @return original task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Reports whether this task has been completed.
     *
     * @return true if completed; false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the text used to display this task.
     *
     * @return description with completion status and any type-specific details
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
