package aigis;

/**
 * Represents the command that ends the Aigis command loop.
 */
public final class ExitCommand extends Command {
    /**
     * Executes the exit command. The application loop handles the exit state.
     *
     * @param tasks The task list, which is unchanged.
     * @param ui The UI, which is unchanged.
     * @param storage The storage service, which is unchanged.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // The command loop checks isExit() after execution.
    }

    /**
     * Checks whether this command ends the application.
     *
     * @return Always {@code true} for an exit command.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
