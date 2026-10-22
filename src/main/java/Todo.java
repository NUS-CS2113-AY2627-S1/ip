/**
 * Represents a task without a deadline or scheduled time.
 */
public class Todo extends Task {
    /**
     * Creates a todo task.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a displayable representation of this todo.
     *
     * @return formatted todo text
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
