package gpt;

/**
 * Represents a task with no date or time attached to it.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete todo with the given description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}