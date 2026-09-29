/**
 * Represents a single task with a description and a done/not-done status.
 */
public class Task {
    /** What the task is, e.g. "read book". */
    protected String description;
    /** Whether the task has been completed. */
    protected boolean isDone;

    /** Creates a task that is not done yet. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns "X" if this task is done, or a space if it is not, for display between brackets. */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /** Returns the text describing what this task is. */
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

    /** Formats the task as "[X] description" or "[ ] description". */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
