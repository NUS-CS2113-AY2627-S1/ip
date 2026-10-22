package wenwen.command;

import wenwen.exception.WenwenException;
import wenwen.storage.Storage;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Represents one user request that can be executed by Wenwen.
 */
public abstract class Command {
    /**
     * Creates a command for a parsed user request.
     */
    protected Command() {
    }

    /**
     * Performs this command using Wenwen's application services.
     *
     * @param tasks task list to read or update
     * @param ui user interface used to show results
     * @param storage storage used to persist changes
     * @throws WenwenException if the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws WenwenException;

    /**
     * Returns whether this command ends the application.
     *
     * @return true only for an exit command
     */
    public boolean isExit() {
        return false;
    }
}
