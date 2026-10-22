package task;

/**
 * Represents a task with a description, category, and completion state.
 */
public abstract class Task {
    private String description = "";
    private boolean isDone = false;
    private final TaskType type;

    /**
     * Creates a task with the given description and category.
     *
     * @param description Description of the task.
     * @param type Category of the task.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
    }

    /**
     * Sets whether this task is complete.
     *
     * @param isDone New completion state.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Sets this task's description.
     *
     * @param description New task description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns this task's description.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is complete.
     *
     * @return {@code true} if this task is complete; otherwise {@code false}.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns this task in the format used for persistent storage.
     *
     * @return Serialized task data.
     */
    public String toFileString() {
        return type.name().charAt(0) + " | " + (isDone ? 1 : 0) + " | " + escapeFileField(description);
    }

    /**
     * Returns text escaped for use as one field in the persistent file format.
     *
     * @param value Text to escape.
     * @return Escaped text suitable for one stored field.
     */
    protected static String escapeFileField(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    /** Returns a display-friendly representation of this task and its completion state. */
    /**
     * Returns a display-friendly representation of this task and its completion state.
     *
     * @return Display text for this task.
     */
    @Override
    public String toString() {
        if (isDone) {
            return "[X] " + getDescription();
        } else {
            return "[ ] " + getDescription();
        }
    }
}
