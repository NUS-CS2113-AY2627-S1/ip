package nga;

/** Represents the command that ends the application. */
public class ExitCommand extends Command {
    /** Creates an exit command. */
    public ExitCommand() {
    }

    /**
     * Returns the exit response without changing the task list or storage.
     *
     * @param taskList ignored because exiting does not modify tasks
     * @param storage ignored because exiting does not save tasks
     * @return a result instructing the UI to exit
     */
    @Override
    public CommandResult execute(TaskList taskList, Storage storage) {
        return new CommandResult(true, "Peace out");
    }

    /**
     * Returns whether this command should end the application.
     *
     * @return always {@code true} for the exit command
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
