package neo.task;

/** Represents a task in Neo's task list. */
public class Task {
    /** The text that describes this task. */
    private String description;

    /** Whether this task has been completed. */
    private boolean isDone;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description Text that describes the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
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
     * Returns the display marker for this task's completion state.
     *
     * @return `X` when the task is done, otherwise a blank space.
     */
    public String getStatus() {
        return (isDone ? "X" : " ");
    }

    /** Returns the description of the task. */
    public String getDescription() {
        return this.description;
    }

    /** Returns a string representation of this task for display. */
    @Override
    public String toString() {
        return "[" + getStatus() + "] " + description;
    }

    /**
     * Returns the completion state and escaped, readable description for storage.
     * Subclasses prepend their task type.
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + escapeForStorage(description);
    }

    /**
     * Escapes special characters while keeping ordinary text human-readable.
     *
     * @param text The original field value.
     * @return The text with backslashes, pipes, and line breaks escaped.
     */
    protected static String escapeForStorage(String text) {
        return text.replace("\\", "\\\\").replace("|", "\\|")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
