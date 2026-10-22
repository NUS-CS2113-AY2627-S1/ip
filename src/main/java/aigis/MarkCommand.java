package aigis;

/**
 * Represents a command that marks a task as done.
 */
public final class MarkCommand extends TaskStatusCommand {
    /**
     * Creates a mark command.
     *
     * @param taskNumber The one-based number of the task to mark.
     */
    public MarkCommand(int taskNumber) {
        super(taskNumber, true, "Marked as done: ");
    }
}
