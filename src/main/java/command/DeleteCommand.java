package command;

/**
 * Deletes one task from the task list.
 */
public class DeleteCommand extends Command {
    /**
     * Creates a delete command from the user's complete input.
     *
     * @param fullCommand Complete delete command entered by the user.
     */
    public DeleteCommand(String fullCommand) {
        super(fullCommand);
    }

    /** Executes this command by deleting the task selected by the user. */
    /**
     * Executes this command by deleting the task selected by the user.
     *
     * @param executor Task operations used to delete the task.
     */
    @Override
    public void execute(CommandExecutor executor) {
        executor.delete(getFullCommand());
    }
}
