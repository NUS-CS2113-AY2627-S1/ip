package chre.task;

/**
 * Todo represents a task without any date/time attached.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo with the given name.
     *
     * @param name the task description
     */
    public Todo(String name) {
        super(name);
    }

    /**
     * Returns the todo with its type icon and completion status.
     * Format: [T][X] name (if done) or [T][ ] name (if not done)
     *
     * @return the formatted todo string
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
