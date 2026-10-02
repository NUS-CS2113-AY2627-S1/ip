package nico.tasks;

/**
 * Represents a task with no date or time attached, e.g. "read book".
 */
public class Todo extends Task {

    /**
     * Creates a todo with the given description.
     *
     * @param description What the todo is about.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }
}