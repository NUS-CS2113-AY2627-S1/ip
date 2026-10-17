package noah.task;

import java.util.Locale;

// This class was implemented with assistance from OpenAI Codex.

/**
 * Represents a task that can be marked as done or not done.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description.
     * New tasks are not done by default.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
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
    public void unmarkAsDone() {
        isDone = false;
    }

    /**
     * Returns the symbol used to display the task's completion status.
     *
     * @return {@code X} if the task is done, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Checks whether the description contains the search text, ignoring letter case.
     * Dates, task-type markers, and completion status are not searched.
     *
     * @param keyword Nonblank literal text to find in the description.
     * @return Whether the description contains the supplied text.
     */
    public boolean containsKeyword(String keyword) {
        return description.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    /**
     * Checks for the same task type and exact description, ignoring completion status.
     * Subclasses with scheduling details extend this comparison to include those details.
     *
     * @param other Task to compare, or null.
     * @return Whether the type and case-sensitive description match.
     */
    public boolean hasSameDetails(Task other) {
        return other != null && getClass() == other.getClass() && description.equals(other.description);
    }

    /**
     * Returns this task in the format used by the data file.
     *
     * @return Serialized task data.
     */
    public String toDataString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the task in the format used when displaying the task list.
     *
     * @return Task status followed by its description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
