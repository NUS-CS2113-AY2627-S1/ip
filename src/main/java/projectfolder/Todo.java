package projectfolder;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {

    /**
     * Creates a todo with the given description.
     *
     * @param description description of the todo
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}