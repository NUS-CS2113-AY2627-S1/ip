package Bastion;
/**
 * A task with a description and completion status.
 * Subclasses add details specific to their task type.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /** Returns the display icon for this task's completion status. */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /** Returns this task's description for searching and display. */
    public String getDescription() {
        return description;
    }

    /** Returns this task in the format used in the user interface. */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns this task in a format suitable for saving to a file.
     *
     * @return a line representing this task
     */
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
