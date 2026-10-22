package nga;

/**
 * Represents a task without a date or time attached to it.
 */
public class Todo extends Task {
    /**
     * Creates an unfinished ToDo with the supplied description.
     *
     * @param description the work to be completed
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the type label for a ToDo.
     *
     * @return {@code "T"}, the storage and display label for ToDos
     */
    @Override
    public String getTaskType() {
        return "T";
    }
}
