package jody;

/**
 * Represents a task without a scheduled date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the todo type marker, completion marker, and description.
     *
     * @return the todo's display text
     */
    @Override
    public String toString() {
        return("[T]" + super.toString());
    }
}
