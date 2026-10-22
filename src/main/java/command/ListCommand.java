package command;

/**
 * Lists the tasks currently stored by the application.
 */
public class ListCommand extends Command {
    /**
     * Creates a list command from the user's complete input.
     *
     * @param fullCommand Complete list command entered by the user.
     */
    public ListCommand(String fullCommand) {
        super(fullCommand);
    }

    /** Executes this command by listing the stored tasks. */
    /**
     * Executes this command by listing the stored tasks.
     *
     * @param executor Task operations used to list the tasks.
     */
    @Override
    public void execute(CommandExecutor executor) {
        executor.list(getFullCommand());
    }
}
