package duke;

/**
 * Represents a task with a description and a done/not-done status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new, not-done task.
     *
     * @param description What the task is about.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon shown for this task's done status.
     *
     * @return "X" if done, a blank space otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /** Marks this task as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not done. */
    public void unmarkAsDone() {
        this.isDone = false;
    }

    /**
     * Returns this task's description.
     *
     * @return The description text.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns this task in the format used to save it to disk.
     *
     * @return The save-format string, without a type prefix.
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
