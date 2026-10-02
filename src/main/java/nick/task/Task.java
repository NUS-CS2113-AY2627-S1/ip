package nick.task;

/**
 * Represents a task with a description and a done status.
 * Subclasses represent specific kinds of tasks such as todos, deadlines, and events.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description that is initially not done.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task: "X" if done, or a space if not done.
     *
     * @return Status icon of the task.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the type icon of the task, e.g. "T" for a todo.
     *
     * @return Type icon of the task.
     */
    public String getTypeIcon() {
        return " ";
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the task formatted for display, showing its type icon,
     * status icon, and description.
     *
     * @return The task as a display string.
     */
    public String toDisplayString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the task encoded as a single line for saving to disk, in the form
     * {@code <type> | <doneFlag> | <description>}. Subclasses append their own fields.
     *
     * @return The task as a save-format line.
     */
    public String toSaveFormat() {
        return getTypeIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }
}
