package command;

/**
 * Finds tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    /**
     * Creates a find command from the user's complete input.
     *
     * @param fullCommand Complete find command entered by the user.
     */
    public FindCommand(String fullCommand) {
        super(fullCommand);
    }

    /** Executes this command by finding tasks that contain the user's keyword. */
    /**
     * Executes this command by finding tasks that contain the user's keyword.
     *
     * @param executor Task operations used to find matching tasks.
     */
    @Override
    public void execute(CommandExecutor executor) {
        executor.find(getFullCommand());
    }
}
