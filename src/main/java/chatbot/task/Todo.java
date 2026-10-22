package chatbot.task;

/** Represents a task without date or time information. */
public class Todo extends Task {
    /** Creates a pending todo task.
     *
     * @param description task description
     */
    public Todo(String description) {
        super(description);
    }
}
