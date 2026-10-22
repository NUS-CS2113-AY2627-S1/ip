package doug.task;

/**
 * Represents a task that has a description but no associated date or time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete todo task.
     *
     * @param description description of the todo
     */
    public Todo(String description) {
        super(Type.TODO, description);
    }

    /**
     * Reconstructs a todo task with an existing completion state.
     *
     * @param description description of the todo
     * @param isDone whether the todo has been completed
     */
    public Todo(String description, boolean isDone) {
        super(Type.TODO, isDone, description);
    }

    /**
     * Converts this todo to its pipe-delimited storage representation.
     *
     * @return serialized todo containing its type, completion state, and description
     */
    @Override
    public String toSaveFormat() {
        return String.format("T|%d|%s", isDone ? 1 : 0, description);
    }
}
