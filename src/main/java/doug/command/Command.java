package doug.command;

import doug.errors.DougException;
import doug.storage.Storage;
import doug.task.TaskList;
import doug.ui.Ui;

/**
 * Represents an executable command that acts on the application's task list.
 */
public abstract class Command {
    /** Indicates whether executing this command should exit the application. */
    protected boolean isExit = false;

    /**
     * Performs this command's operation.
     *
     * @param taskList task list on which the command operates
     * @param ui user interface used to communicate with the user
     * @param storage storage used to persist task data
     * @throws DougException if the command cannot be completed
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws DougException;

    /**
     * Returns whether the application should exit after this command.
     *
     * @return {@code true} if the application should exit; {@code false} otherwise
     */
    public boolean isExit() {
        return isExit;
    }
}
