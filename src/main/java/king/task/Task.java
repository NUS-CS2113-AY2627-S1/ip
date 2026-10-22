package king.task;

/**
 * Represents a single task in the King chatbot's list.
 *
 * <p>Each task has a description and a done flag. Tasks start as not done;
 * the flag can be toggled with {@link #markAsDone()} and {@link #markAsNotDone()}.
 * {@link #getStatusIcon()} renders the done state as a checkbox-style icon
 * for use when printing the task list.
 */
public class Task {

    /** Human-readable text of the task. */
    protected String description;

    /** Whether the task has been completed. */
    protected boolean isDone;

    /**
     * Creates a new task with the given description, initially marked as not done.
     *
     * @param description the text of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done (reverts a previous completion).
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns a single-character icon representing the done state:
     * {@code "X"} for done, {@code " "} for not done.
     *
     * @return Completion status icon.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the description of this task.
     *
     * @return Description formatted for display.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the original description without display-only date or time suffixes.
     *
     * @return Original description without display suffixes.
     */
    public String getRawDescription() {
        return description;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return Whether the task is complete.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a single character representing the task type.
     * Subclasses should override this method to return their specific type.
     *
     * @return the task type character (e.g., "T", "D", "E").
     */
    public String getTaskType() {
        return "T"; // default task type
    }
}
