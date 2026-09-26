package tasks;

/**
 * Represents a task without a deadline or event time range.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete ToDo.
     *
     * @param description Text describing the work to do.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the ToDo type marker.
     *
     * @return The marker T.
     */
    @Override
    public String getTypeIcon() {
        return "T";
    }
}
