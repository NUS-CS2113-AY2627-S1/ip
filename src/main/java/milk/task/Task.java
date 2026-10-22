package milk.task;

/**
 * Parent class inherited by the various task classes.
 */
public class Task {
    protected String description;
    protected boolean marked;

    /**
     * Construct of the parent class Task.
     * Sets the task to unmarked by default.
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.marked = false;
    }

    /**
     * Gets the status icon of the ask (whether it is marked or unamrked).
     * @return Status icon.
     */
    public String getStatusIcon() {
        return (marked ? "[X]" : "[ ]");
    }

    /**
     * Gets the description of the task.
     * @return The description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * @param description The description of the task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets whether the task is marked or not.
     * @return Whether the task is marked or not.
     */
    public boolean isMarked() {
        return marked;
    }

    /**
     * Sets whether the task is marked or not.
     * @param marked Whether the task is marked or not.
     */
    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    /**
     * A placeholder method that is overridden by Task's children.
     * @return Placeholder icon.
     */
    public String getTaskIcon() {
        return "[ ]";
    }

    /**
     * Overrides the toString function.
     * @return The description of the task.
     * */
    @Override
    public String toString() {
        return description;
    }
}
