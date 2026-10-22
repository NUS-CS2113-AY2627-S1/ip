package cracker;

/**
 * Represents a command that adds a deadline task.
 */
public class DeadlineCommand extends AddCommand {
    /**
     * Creates a command that adds a deadline task.
     *
     * @param description deadline description
     * @param by deadline due time
     */
    public DeadlineCommand(String description, String by) {
        super(new Deadline(description, by));
    }
}
