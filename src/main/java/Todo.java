/**
 * A task with only a description and no date or time.
 */
public class Todo extends Task {
    /** Creates a todo that is not done yet. */
    public Todo(String description) {
        super(description);
    }

    /** Formats the todo as, e.g., "[T][ ] read book". */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
