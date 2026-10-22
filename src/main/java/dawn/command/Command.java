package dawn.command;

import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /**
     * Executes the command against the provided task list, user interface, and storage.
     *
     * @param tasks the current task list.
     * @param ui the user interface for displaying responses.
     * @param storage the storage handler for persisting updates.
     * @throws DawnException if execution fails.
     */
    public abstract void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException;

    /**
     * Indicates whether this command is an exit command that terminates the application.
     *
     * @return {@code true} if the application should exit, {@code false} otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
