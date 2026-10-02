package buddy.task;

/**
 * Represents a single task that Buddy can keep track of.
 *
 * <p>A task has a text description and a "done" status that starts as
 * false and can be toggled via {@link #markAsDone()} and
 * {@link #markAsNotDone()}.
 *
 * <p>The fields are {@code protected} rather than {@code private} so that
 * future subclasses (e.g. more specific kinds of tasks) can access them
 * directly.
 */
public class Task {
    /** The text describing what this task is, e.g. "read book". */
    protected String description;

    /** True if this task has been marked as done; false otherwise. */
    protected boolean isDone;

    /**
     * Creates a new task with the given description. New tasks start out
     * not done.
     *
     * @param description the text describing this task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns a one-character icon showing whether this task is done.
     *
     * @return "X" if this task is done, or " " if it is not
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the description of this task.
     *
     * @return the task's description text
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }

    /**
     * Returns this task encoded as a single line for saving to disk, in
     * the form "&lt;done&gt; | &lt;description&gt;". Subclasses prepend
     * their type letter and append any extra fields they have.
     *
     * @return this task's shared fields encoded as a save-file line
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
