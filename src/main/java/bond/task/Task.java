package bond.task;

import java.util.Locale;

/**
 * Represents a task with a description and completion status.
 */
public class Task {

    protected static final String DATA_FIELD_DELIMITER = " | ";

    private final String description;
    private boolean isDone;

    /**
     * Creates a task that is initially not done.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol used to display the task's completion status.
     *
     * @return "X" when done, or a space when not done.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
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
     * Returns whether this task has been completed.
     *
     * @return True if this task is completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns whether this task's description contains a keyword, ignoring case.
     *
     * @param keyword Keyword or phrase to find in the description.
     * @return True if the description contains the keyword.
     */
    public boolean matchesDescription(String keyword) {
        return description.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns the task data shared by every task type for file storage.
     *
     * @return Completion value and description separated by file delimiters.
     */
    public String toDataString() {
        String completionValue = isDone ? "1" : "0";
        return completionValue + DATA_FIELD_DELIMITER + escapeDataField(description);
    }

    /**
     * Escapes characters that have special meaning in the storage format.
     *
     * @param field Task field to encode for storage.
     * @return Encoded task field.
     */
    protected static String escapeDataField(String field) {
        return field.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Returns the task's status icon followed by its description.
     *
     * @return Display form of this task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
