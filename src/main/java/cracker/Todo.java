package cracker;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete to-do task with the given description.
     *
     * @param description text describing the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this to-do task in the display format.
     *
     * @return this task's type, status, and description
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
