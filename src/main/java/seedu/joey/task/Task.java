package seedu.joey.task;

/**
 * Represents a task in the task list.
 * A task has a description and a status indicating whether it is done.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new task with the given description. Newly created tasks
     * start out as not done.
     *
     * @param description Human-readable description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon representing the completion status of this task.
     *
     * @return "X" if the task is done, a blank space otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the icon representing the type of this task.
     *
     * @return A blank space, as a general task has no specific type.
     */
    public String getTypeIcon() {
        return " ";
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done yet.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the string representation of this task,
     * in the form [TypeIcon][StatusIcon] description.
     *
     * @return The formatted description of this task.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the representation of this task used in the save file,
     * in the form {@code TYPE | DONE | DESCRIPTION}.
     *
     * @return The pipe-delimited save-file line for this task.
     */
    public String toFileFormat() {
        return getTypeIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }
}