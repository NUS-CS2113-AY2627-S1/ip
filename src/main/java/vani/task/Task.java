package vani.task;

/**
 * Represents one task managed by the Vani task list.
 *
 * <p>A task starts as incomplete. Its completion state and description can be
 * changed through the mark methods and description setter.</p>
 */
public class Task {

    private String description;
    private boolean isDone;

    /**
     * Creates a new incomplete task with the supplied description.
     *
     * @param description the text entered for the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task description used for display, searching, and saving.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Replaces the task description without changing its completion state.
     *
     * @param description the new text describing the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the symbol used by the user interface to display this task's
     * completion state.
     *
     * @return "X" if the task is complete, or a single space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as incomplete so that it can be completed again later.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task's completion status and description for display.
     *
     * @return the status in brackets followed by the description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
