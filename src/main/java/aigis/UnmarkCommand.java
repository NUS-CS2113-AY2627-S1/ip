package aigis;

/**
 * Represents a command that marks a task as not done.
 */
public final class UnmarkCommand extends TaskStatusCommand {
    /**
     * Creates an unmark command.
     *
     * @param taskNumber The one-based number of the task to unmark.
     */
    public UnmarkCommand(int taskNumber) {
        super(taskNumber, false, "Unmarked as done: ");
    }
}
