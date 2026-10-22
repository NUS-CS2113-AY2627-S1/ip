package command;

/**
 * Ends the current application session.
 */
public class ExitCommand extends Command {
    /**
     * Creates an exit command from the user's complete input.
     *
     * @param fullCommand Complete exit command entered by the user.
     */
    public ExitCommand(String fullCommand) {
        super(fullCommand);
    }

    /** Executes this command without performing a task operation. */
    /**
     * Executes this command without performing a task operation.
     *
     * @param executor Task operations available to the command.
     */
    @Override
    public void execute(CommandExecutor executor) {
        // No task operation is needed when exiting.
    }

    /** Returns whether this command should end the application. */
    /**
     * Returns whether this command should end the application.
     *
     * @return Always {@code true}.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
