package tasks;

/**
 * Stores a task's description and completion status for specialized task types.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates an incomplete task.
     *
     * @param description Text describing the work to do.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the completion marker used in console task displays.
     *
     * @return X when complete, or a single space when incomplete.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Reports whether this task has been completed.
     *
     * @return True if the task is complete.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the task complete, including when it is already complete.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task incomplete, including when it is already incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the description searched by find, without dates or status markers.
     *
     * @return The task's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the default task type marker, which subclasses can specialize.
     *
     * @return The default ToDo marker T.
     */
    public String getTypeIcon() {
        return "T";
    }

    /**
     * Returns display text, which dated task subclasses extend with their details.
     *
     * @return The plain description for a task without additional details.
     */
    public String getFullDescription() {
        return description;
    }
}
