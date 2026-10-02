package chre.command;

import chre.data.TaskList;
import chre.ui.Ui;
import chre.storage.Storage;
import chre.exception.ChreException;

/**
 * Abstract base class for all commands.
 * Each command represents a user action that can be executed.
 */
public abstract class Command {
    /**
     * Executes the command with access to the task list, UI, and storage.
     *
     * @param taskList the task list to operate on
     * @param ui the UI for displaying output
     * @param storage the storage for saving/loading tasks
     * @throws ChreException if the command execution fails
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws ChreException;

    /**
     * Returns whether this command should exit the application.
     *
     * @return true if this is an exit command, false otherwise
     */
    public boolean isExit() {
        return false;
    }
}
