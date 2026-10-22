package wenwen.task;

import java.time.LocalDate;

/**
 * Represents one task in the chatbot's task list.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task that is initially not done.
     *
     * @param description The text describing the task.
     */
    protected Task(String description) {
        this(description, false);
    }

    /**
     * Creates a task with its saved completion state.
     *
     * @param description The text describing the task.
     * @param isDone Whether the task is completed.
     */
    protected Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return "X" if the task is done, otherwise a space.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the text describing this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is completed.
     *
     * @return True if this task is completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns whether this task occurs on the supplied date.
     *
     * @param date date to check
     * @return true if the task has a date that matches the supplied date
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns the one-letter symbol for this task type.
     *
     * @return The task type symbol.
     */
    protected abstract String getTypeIcon();

    /**
     * Returns this task in the display format used by the chatbot.
     *
     * @return The formatted task status and description.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
