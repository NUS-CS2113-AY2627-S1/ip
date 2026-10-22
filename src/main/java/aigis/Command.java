package aigis;

/**
 * Represents a parsed user command that can be executed by Aigis.
 */
public abstract class Command {
    /**
     * Executes this command against the application's task list and services.
     *
     * @param tasks The task list changed or queried by the command.
     * @param ui The UI used to display the command result.
     * @param storage The storage service available to the command.
     * @throws AigisException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws AigisException;

    /**
     * Checks whether this command ends the application.
     *
     * @return {@code true} if the command requests application exit.
     */
    public boolean isExit() {
        return false;
    }
}
