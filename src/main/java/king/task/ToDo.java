package king.task;

/**
 * Represents a todo task with no associated date or deadline.
 *
 * <p>A ToDo is a simple task with just a description.
 * It is displayed with the type icon {@code [T]} and inherits its
 * done-state behavior from {@link Task}.
 */
public class ToDo extends Task {

    /**
     * Creates a new ToDo task with the given description,
     * initially marked as not done.
     *
     * @param description the text of the task.
     */
    public ToDo(String description) {
        super(description);
    }
}
