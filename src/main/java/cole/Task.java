package cole;

/**
 * Represents a task the user wants to keep track of.
 * A task has a description and can be marked as done or not done.
 * Subclasses add details such as a deadline date or an event period.
 */
public class Task {

    private String description;
    private boolean isDone;

    /**
     * Creates a task with the given description. New tasks start as not done.
     *
     * @param description what the task is about
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol shown inside the status box when the task is listed.
     *
     * @return "X" if the task is done, or a space if it is not
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the task as shown to the user, e.g. "[X] read book".
     *
     * @return the status box followed by the description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been marked as done.
     *
     * @return true if the task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the part of the data file line shared by all task types,
     * in the form "isDone | description" (isDone is 1 or 0).
     * Subclasses prepend their type letter and append their own fields.
     *
     * @return the common fields of this task in save format
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
