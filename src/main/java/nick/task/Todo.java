package nick.task;

/**
 * Represents a todo: a task without any date or time attached to it.
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

    @Override
    public String getTypeIcon() {
        return "T";
    }
}
