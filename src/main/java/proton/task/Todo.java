package proton.task;

/**
 * Represents a task without an associated date or time.
 */
public class Todo extends Task {
    /**
     * Creates a ToDo task that is initially not completed.
     *
     * @param description Description of the ToDo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this task with its ToDo type marker.
     *
     * @return The formatted ToDo task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
