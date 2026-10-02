package buddy.task;

/**
 * Represents a simple task with no date or time attached, other than its
 * description.
 */
public class Todo extends Task {

    /**
     * Creates a new todo with the given description.
     *
     * @param description the text describing this todo
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toSaveFormat() {
        return "T | " + super.toSaveFormat();
    }
}
