package hedy.task;

/**
 * A task that only has a description, with no date or time.
 */
public class Todo extends Task {

    /**
     * Creates a todo.
     *
     * @param description what needs to be done
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
