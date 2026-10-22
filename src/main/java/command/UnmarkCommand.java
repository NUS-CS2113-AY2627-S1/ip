package command;

/**
 * Marks one task as not done.
 */
public class UnmarkCommand extends Command {
    /**
     * Creates an unmark command from the user's complete input.
     *
     * @param fullCommand Complete unmark command entered by the user.
     */
    public UnmarkCommand(String fullCommand) {
        super(fullCommand);
    }

    /** Executes this command by marking the selected task as not done. */
    /**
     * Executes this command by marking the selected task as not done.
     *
     * @param executor Task operations used to unmark the task.
     */
    @Override
    public void execute(CommandExecutor executor) {
        executor.unmark(getFullCommand());
    }
}
