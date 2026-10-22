package dook;

/**
 * Represents a todo task without a specific deadline or event time.
 */
public class Todo extends Task {

    /**
     * Creates a new todo task with the specified description.
     *
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this todo task.
     *
     * @return the todo task with its type and completion status
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
