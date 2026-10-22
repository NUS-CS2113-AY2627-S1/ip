package jamal.task;

/**
 * Base class for every type of task.
 *
 * A task knows its description and whether it is done. Its children
 * ({@link Todo}, {@link Deadline}, {@link Event}) add any date/time information
 * and supply their own one-letter type icon.
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates a new task with the given description.
     * Task is initially not done.
     *
     * @param description Text of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the text of the task.
     *
     * @return Description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task is done.
     *
     * @return True if the task is marked as done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the task as done or not done.
     *
     * @param isDone True to mark the task as done, false to unmark it.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns the single letter shown in the first pair of brackets,
     * actual return is overridden by child classes.
     *
     * @return Type icon of this task.
     */
    public abstract String getTypeIcon();

    /**
     * Returns the symbol shown in the second pair of brackets.
     *
     * @return "X" if the task is done, a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task as one line of the save file, e.g. {@code T | 1 | read book}.
     * Subclasses append their date/time fields by overriding this and calling
     * {@code super.toFileString()} first, the same way {@link #toString()} works.
     *
     * @return Line representing this task in the save file.
     */
    public String toFileString() {
        return getTypeIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the task formatted for display, e.g. {@code [T][X] read book}.
     * Subclasses append their date/time details by overriding this and calling
     * {@code super.toString()} first.
     *
     * @return Display text of this task.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
