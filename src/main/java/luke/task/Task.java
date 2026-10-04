package luke.task;

import java.util.Locale;

/**
 * Represents a task and whether it has been completed.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon that identifies this task's type.
     *
     * @return Task-type icon.
     */
    protected abstract String getTypeIcon();

    /**
     * Returns the task in the format used for data storage.
     *
     * @return String representation of the task for storage.
     */
    public abstract String toDataString();

    /**
     * Returns the task description.
     *
     * @return Description of the task.
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Returns whether the task has been completed.
     *
     * @return True if the task is completed.
     */
    protected boolean isDone() {
        return isDone;
    }

    /**
     * Returns the icon that represents the task's completion status.
     *
     * @return {@code X} when the task is done, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns whether the description contains the specified keyword.
     * Matching is case-insensitive.
     *
     * @param keyword Keyword to find in the description.
     * @return True if the description contains the keyword.
     */
    public boolean containsKeyword(String keyword) {
        String normalizedDescription = description.toLowerCase(Locale.ROOT);
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return normalizedDescription.contains(normalizedKeyword);
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
