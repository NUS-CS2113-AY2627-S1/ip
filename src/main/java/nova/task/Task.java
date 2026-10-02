package nova.task;
/**
 * Represents a single task with a description and a completion status.
 * This is the abstract base class for all task types (Todo, Deadline, Event).
 */
public abstract class Task {

    protected String description;
    protected boolean isDone;

    /**
     * Creates a new, incomplete task with the given description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns whether this task has been marked as done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description of this task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status icon representing whether this task is done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the letter representing this task's type, e.g. "T", "D", "E".
     * Each subclass must define its own type icon.
     */
    public abstract String getTypeIcon();

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
     * Returns this task as a single line of text suitable for saving to disk.
     * Subclasses append their own extra fields.
     */
    public String toSaveString() {
        return getTypeIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the display representation of this task, e.g. "[T][X] read book".
     * Subclasses extend this by appending their own extra details.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}