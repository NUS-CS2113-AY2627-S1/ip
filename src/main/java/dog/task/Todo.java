package dog.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {

    /**
     * Creates a todo with the given description.
     *
     * @param description Description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the todo type marker, status, and description.
     *
     * @return Display text beginning with {@code [T]}.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
