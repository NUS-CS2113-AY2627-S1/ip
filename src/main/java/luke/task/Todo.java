package luke.task;

/**
 * Represents a task without a deadline or scheduled time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete todo with the given description.
     *
     * @param description Description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }

    @Override
    public String toDataString() {
        String status = isDone() ? "1" : "0";

        return "T | " + status + " | " + getDescription();
    }

}
