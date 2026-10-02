package task;

/** Represents a task without a date.
 */
public class Todo extends Task {
    /** Creates a todo with the supplied description.
     * @param description text entered by the user
     */
    public Todo(String description) {
        super(description);
    }

    /** Returns the todo identifier used in task display.
     * @return {@code 'T'}
     */
    public char getIdentifier() {
        return 'T';
    }

}
