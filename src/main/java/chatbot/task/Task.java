package chatbot.task;

/**
 * Represents a task in the task list.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a pending task with the given description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return X for a done task, or a space for a pending task
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns whether this task has been completed.
     *
     * @return true if this task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the task type and timing information used for display.
     *
     * @return the formatted task description
     */
    public String getDisplayText() {
        return description;
    }

    /**
     * Returns the task type icon.
     *
     * @return the one-letter task type
     */
    public String getTaskType() {
        return "T";
    }
}
