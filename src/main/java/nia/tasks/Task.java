package nia.tasks;

/**
 * Represents a single task: its description and whether it has been completed.
 * New tasks start as not done.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /** Creates a new, not-done task with the given description. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns this task's description. */
    public String getDescription() {
        return this.description;
    }

    /** Returns true if this task has been marked done. */
    public boolean isDone() {
        return this.isDone;
    }

    /** Returns "X" if the task is done, or a blank space otherwise (for display, e.g. "[X]"/"[ ]"). */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /** Returns the single-letter tag ("T"/"D"/"E") identifying this task's type; a blank space by default. */
    public String getTag() {
        return " ";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }

    /** Marks this task as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        this.isDone = false;
    }
}