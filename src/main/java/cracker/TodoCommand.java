package cracker;

/**
 * Represents a command that adds a to-do task.
 */
public class TodoCommand extends AddCommand {
    /**
     * Creates a command that adds a to-do with the supplied description.
     *
     * @param description to-do description
     * @throws CrackerException if the description is empty
     */
    public TodoCommand(String description) throws CrackerException {
        super(createTask(description));
    }

    /**
     * Creates the to-do task after validating its description.
     *
     * @param description to-do description
     * @return validated to-do task
     * @throws CrackerException if the description is empty
     */
    private static Task createTask(String description) throws CrackerException {
        if (description.isEmpty()) {
            throw new CrackerException("A to-do needs a description. Try: todo buy groceries");
        }
        return new Todo(description);
    }
}
