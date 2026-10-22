package command;

/**
 * Represents a user instruction that can be executed by the application.
 */
public abstract class Command {
    private final String fullCommand;

    /**
     * Creates a command from the user's complete input.
     *
     * @param fullCommand Complete command entered by the user.
     */
    protected Command(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes this command using the supplied command operations.
     *
     * @param executor Task operations available to the command.
     */
    public abstract void execute(CommandExecutor executor);

    /**
     * Returns whether this command should end the application.
     *
     * @return {@code true} if this command ends the application; otherwise {@code false}.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Returns the user's complete command.
     *
     * @return Complete command entered by the user.
     */
    protected String getFullCommand() {
        return fullCommand;
    }
}
