package maple.task;

import java.util.Locale;

/**
 * Represents a task in Maple's task list.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Constructs a task with the given description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns whether this task has been marked as done.
     *
     * @return true if the task is done, or false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the icon that represents the task's completion status.
     *
     * @return {@code X} for a completed task, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markNotDone() {
        isDone = false;
    }

    /**
     * Returns whether the description contains the given keyword, ignoring case.
     *
     * @param keyword the keyword to find.
     * @return true if the description contains the keyword, or false otherwise.
     */
    public boolean matches(String keyword) {
        String normalizedDescription = description.toLowerCase(Locale.ROOT);
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return normalizedDescription.contains(normalizedKeyword);
    }

    /**
     * Returns the portion of this task's data file format shared by all tasks.
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
