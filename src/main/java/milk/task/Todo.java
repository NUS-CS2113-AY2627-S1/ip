package milk.task;

/**
 * Represents a task without a deadline or "from" and "to" values.
 */
public class Todo extends Task {

    /**
     * Constructor of the Todo task.
     * @param description The Todo's description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Getter of the Todo task icon.
     * Overrides the parent class's function.
     * @return Todo task icon.
     */
    @Override
    public String getTaskIcon() {
        return "[T]";
    }

    /**
     * Overrides the toString function.
     * @return The task icon, followed by the status icon, followed by the description.
     */
    @Override
    public String toString() {
        return getTaskIcon() + getStatusIcon() + " " + super.toString();
    }
}
