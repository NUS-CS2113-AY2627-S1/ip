package aigis.task;

/**
 * Represents an objective managed by Aigis.
 */
public class Task {
    /** The text describing this task. */
    private final String description;
    /** Whether this task has been completed. */
    private boolean isDone;

    /**
     * Creates an unfinished task with the given description.
     *
     * @param description The text describing the task.
     * @throws IllegalArgumentException If the description is null or blank.
     */
    public Task(String description) {
        validateField(description, "Task description");
        this.description = description;
        this.isDone = false;
    }

    /**
     * Validates a text field used to construct a task.
     *
     * @param value The value to validate.
     * @param fieldName The name of the field used in the error message.
     * @throws IllegalArgumentException If the value is null or blank.
     */
    protected static void validateField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank.");
        }
        if (value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            throw new IllegalArgumentException(fieldName + " cannot contain line breaks.");
        }
    }

    /**
     * Returns the marker used to show whether this task is complete.
     *
     * @return {@code X} for a completed task, or a blank space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return This task's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if this task is complete.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Sets whether this task is complete.
     *
     * @param isDone The new completion state.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns this task in the format used by the data file.
     *
     * @return The serialized form of this task.
     */
    public String toStorageString() {
        return getStorageType() + " / " + (isDone ? "1" : "0") + " / " + getStorageDetails();
    }

    /**
     * Returns the type marker written before a task in the data file.
     *
     * @return The task type marker.
     */
    protected String getStorageType() {
        return "Task";
    }

    /**
     * Returns the task details written after the status in the data file.
     *
     * @return The task details.
     */
    protected String getStorageDetails() {
        return description;
    }

    /**
     * Returns the display form used by Aigis when showing a task.
     *
     * @return The status marker followed by the task description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
