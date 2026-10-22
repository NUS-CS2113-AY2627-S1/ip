package jamal.task;

/**
 * A task without any date/time attached to it, e.g., visit new theme park.
 */
public class Todo extends Task {
    /**
     * Creates a todo with the given description.
     *
     * @param description Text of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }
}
