package yuwei.task;

/**
 * A task the user wants to keep track of, which can be marked as done or not done.
 *
 * <p>This class is abstract because every real task is a more specific kind:
 * a {@link ToDo}, {@link Deadline} or {@link Event}. Each subclass decides how it is
 * labelled ({@link #getType()}) and how it is written to the data file ({@link #toFileString()}).
 */
public abstract class Task {
    /** What the task is, e.g. {@code read book}. Subclasses use it in their file format. */
    protected String description;
    /** Whether the user has marked this task as done. */
    protected boolean isDone;

    /** Creates a task with the given description that is not yet done. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns {@code "[X] "} if this task is done and {@code "[ ] "} otherwise. */
    public String getStatusIcon() {
        return (isDone ? "[X] " : "[ ] ");
    }

    /** Marks this task as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /** Returns the description of this task, e.g. {@code "read book"}. */
    public String getDescription() {
        return description;
    }

    /** Returns the label that shows what kind of task this is, e.g. {@code "[T]"}. */
    public abstract String getType();

    /** Returns "1" if this task is done and "0" otherwise, as stored in the data file. */
    protected String getDoneFlag() {
        return isDone ? "1" : "0";
    }

    /** Returns this task as one line of the data file, e.g. {@code T | 1 | read book}. */
    public abstract String toFileString();

    /** Returns this task as shown to the user, e.g. {@code [T][X] read book}. */
    @Override
    public String toString() {
        return getType() + getStatusIcon() + description;
    }
}
