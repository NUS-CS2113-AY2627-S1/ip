package hedy.task;

/**
 * A task with a description and a done / not-done status.
 * Todo, Deadline, and Event share this behaviour.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task that is not done yet.
     *
     * @param description text that identifies the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description shown in the task list.
     * {@code find} searches this text.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the icon shown in the task list.
     * {@code X} means done, and a blank means not done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Updates whether this task is done.
     *
     * @param done true if the task should be marked done
     */
    public void setDone(boolean done) {
        this.isDone = done;
    }

    /**
     * Returns the done flag and description in the save-file format.
     * Subclasses put their type letter and extra fields around this text.
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
