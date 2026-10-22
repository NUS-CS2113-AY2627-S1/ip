package isa.task;

/**
 * Represents a task without a deadline or scheduled time.
 */
public class Todo extends Task {
    /**
     * Creates a todo with the given description.
     *
     * @param description Text describing the todo.
     */
    public Todo(String description) {

        super(description);
    }

    /**
     * Returns the todo with its type and status for display.
     *
     * @return Display representation of this todo.
     */
    @Override
    public String toString() {

        return "[T]" + super.toString();
    }
}
