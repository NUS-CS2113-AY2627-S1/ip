package aglio.command;

import aglio.exception.AglioException;
import aglio.storage.Storage;
import aglio.task.TaskList;
import aglio.ui.Ui;

/**
 * Represents a parsed user command that can be executed.
 * Subclasses implement {@link #execute} to carry out specific actions.
 */
public abstract class Command {

    /**
     * Executes this command using the given task list, ui, and storage.
     *
     * @param tasks   the task list to operate on
     * @param ui      the ui for displaying responses
     * @param storage the storage for persisting changes
     * @throws AglioException if the command cannot be carried out
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws AglioException;

    /**
     * Returns true if this command should cause the program to exit.
     * Only {@link ExitCommand} overrides this to return true.
     *
     * @return true if the program should exit after this command
     */
    public boolean isExit() {
        return false;
    }
}
