package clanky.task;

/**
 * Represents a generic task with a description and completion status.
 * This is the base class for all task types in Clanky, including
 * {@link Todo}, {@link Deadline}, and {@link Event}.
 * Subclasses are expected to override {@link #getTaskType()} and,
 * where relevant, {@link #toString()} and {@link #toSaveFormat()} to
 * reflect their own additional fields.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description a description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon representing whether this task is done.
     *
     * @return "X" if the task is marked done, or a blank space if not.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns this task's description.
     *
     * @return the task's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Marks this task as done.
     */
    public void setMark() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void setUnmark() {
        this.isDone = false;
    }

    /**
     * Returns the single-letter type icon used to identify this task's type
     * in list and save-file output. Subclasses should override this to return
     * their own identifying letter (i.e. "T", "D", "E").
     *
     * @return a blank space by default, intended to be overridden.
     */
    public String getTaskType() {
        return " ";
    }

    /**
     * Returns a user-facing string representation of this task, including
     * its type icon, status icon, and description.
     *
     * @return the formatted display string for this task.
     */
    @Override
    public String toString() {
        return "[" + getTaskType() + "][" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns a string representation of this task suitable for saving to
     * disk, using a pipe-delimited format: type | done-flag | description.
     * Subclasses with additional fields should extend this via {@code super}.
     *
     * @return the save-format string for this task.
     */
    public String toSaveFormat() {
        return getTaskType() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

}
