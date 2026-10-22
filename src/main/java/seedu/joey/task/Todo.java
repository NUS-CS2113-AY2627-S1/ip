package seedu.joey.task;
/**
 * Represents a task without any date or time attached to it.
 */
public class Todo extends Task {

    /**
     * Creates a new todo with the given description.
     *
     * @param description Human-readable description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the type icon for a todo, which is {@code "T"}.
     *
     * @return The single-character type icon.
     */
    @Override
    public String getTypeIcon() {
        return "T";
    }
}