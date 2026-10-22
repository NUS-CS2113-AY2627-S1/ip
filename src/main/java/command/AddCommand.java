package command;

/**
 * Adds a todo, deadline, or event task.
 */
public class AddCommand extends Command {
    /**
     * Creates an add command from the user's complete input.
     *
     * @param fullCommand Complete add command entered by the user.
     */
    public AddCommand(String fullCommand) {
        super(fullCommand);
    }

    /** Executes this command by adding the task described by the user's input. */
    /**
     * Executes this command by adding the task described by the user's input.
     *
     * @param executor Task operations used to add the task.
     */
    @Override
    public void execute(CommandExecutor executor) {
        executor.add(getFullCommand());
    }
}
