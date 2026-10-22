package usagi;

/**
 * Represents a task with a description and a done/not-done status.
 *
 * This is the common base class for all task types. Each concrete subclass
 * supplies its own type icon (e.g. {@code "T"} for a todo), so that the
 * formatting logic shared by all task types can live here.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new, not-done task with the given description.
     *
     * @param description Text describing the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the single-character icon identifying this task's type.
     *
     * @return {@code "T"}, {@code "D"} or {@code "E"} depending on the subclass.
     */
    public abstract String getTypeIcon();

    /**
     * Returns the single-character icon representing this task's done status.
     *
     * @return {@code "X"} if the task is done, or a single space otherwise.
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
     * Returns this task written as one line of the save file.
     *
     * Subclasses that have extra details append them to this line, in the same
     * order that {@code Storage} reads them back.
     *
     * @return Line of the form {@code "T | 1 | read book"}, where the middle
     *         field is 1 when the task is done and 0 when it is not.
     */
    public String toFileFormat() {
        return getTypeIcon() + Storage.FIELD_SEPARATOR + (isDone ? "1" : "0")
                + Storage.FIELD_SEPARATOR + description;
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
