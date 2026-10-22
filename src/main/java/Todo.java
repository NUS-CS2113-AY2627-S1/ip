/** Represents a simple task without a date or time constraint. */
public class Todo extends Task {
    /**
     * Creates a new todo task with the given description.
     *
     * @param body Description of the todo task.
     */
    public Todo(String body) {
        super(body);
    }

    /**
     * Creates a new todo task from a string representation.
     *
     * @param body String representation of the todo task.
     * @return A new Todo object.
     */
    /** Creates a todo task from a non-empty description. */
    public static Todo fromString(String body) throws IllegalEventException {
        if (body == null || body.isBlank()) {
            throw new IllegalEventException("Todo description cannot be empty.");
        }
        return new Todo(body.trim());
    }

    @Override 
    public char getStatusIcon() {
        return 'T';
    }
    
}
