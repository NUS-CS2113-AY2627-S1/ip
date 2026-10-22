package slowbro;

/** Represents a task without a deadline or event period. */
public class Todo extends Task {
    private static final String TODO_PREFIX = "[T]";

    /**
     * Creates a task with the given description.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return TODO_PREFIX + super.toString();
    }
}
