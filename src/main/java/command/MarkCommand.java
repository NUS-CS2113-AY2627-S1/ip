package command;

/**
 * Marks one task as done.
 */
public class MarkCommand extends Command {
    /**
     * Creates a mark command from the user's complete input.
     *
     * @param fullCommand Complete mark command entered by the user.
     */
    public MarkCommand(String fullCommand) {
        super(fullCommand);
    }

    /** Executes this command by marking the selected task as done. */
    /**
     * Executes this command by marking the selected task as done.
     *
     * @param executor Task operations used to mark the task.
     */
    @Override
    public void execute(CommandExecutor executor) {
        executor.mark(getFullCommand());
    }
}
