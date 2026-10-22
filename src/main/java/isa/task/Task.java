package isa.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Text describing the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol used to display the completion status.
     *
     * @return X if completed, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return Description of this task.
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task in the format used by the data file.
     *
     * @return File representation of this task.
     */
    public String toDataString() {
        return getBaseDataString("T");
    }

    /**
     * Returns the shared file data for a particular task type.
     *
     * @param taskType Letter representing the task type.
     * @return Shared file representation of the task.
     */
    protected String getBaseDataString(String taskType) {
        String status = isDone ? "1" : "0";
        return taskType + " | " + status + " | " + escapeDataField(description);
    }

    /**
     * Escapes characters that have a special meaning in the data file.
     *
     * @param value Field value to escape.
     * @return Escaped field value.
     */
    protected String escapeDataField(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    /**
     * Returns the task status and description for display.
     *
     * @return Display representation of this task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
