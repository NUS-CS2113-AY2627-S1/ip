package bryte.command;

import bryte.Storage;
import bryte.TaskList;
import bryte.Ui;
import bryte.exception.BryteException;

/**
 * Represents an executable command.
 */
public abstract class Command {
    /**
     * Constructs a new {@code Command}.
     */
    public Command() {
    }

    /**
     * Executes the command.
     *
     * @param tasks   The task list.
     * @param ui      The user interface.
     * @param storage The storage for saving/loading tasks.
     * @throws BryteException If an error occurs during execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws BryteException;

    /**
     * Checks if this command should exit the application.
     *
     * @return True if the application should exit, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
