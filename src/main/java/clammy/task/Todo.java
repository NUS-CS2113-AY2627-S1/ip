package clammy.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete task without a date or time.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the todo type, completion status, and description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
