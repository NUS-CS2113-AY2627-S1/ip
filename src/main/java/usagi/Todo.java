package usagi;

/**
 * Represents a task that has no date or time attached to it.
 */
public class Todo extends Task {
    /**
     * Creates a new, not-done todo with the given description.
     *
     * @param description Text describing the todo.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }
}
