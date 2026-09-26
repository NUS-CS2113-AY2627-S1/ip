package moistbot.task;

/**
 * Represents a basic todo item with no date or time attached.
 * Extends Task to provide a convenient constructor for creating simple to-do tasks.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task with the given description.
     *
     * @param description The description of the todo item
     */
    public Todo(String description) {
        super(description, TYPE_TODO);
    }
}
