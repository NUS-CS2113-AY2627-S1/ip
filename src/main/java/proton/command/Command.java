package proton.command;

import proton.exception.ProtonException;
import proton.storage.Storage;
import proton.task.TaskList;
import proton.ui.Ui;

/**
 * Represents an executable user action, separate from its console syntax.
 */
public abstract class Command {
    /**
     * Executes this action using the application's tasks, UI, and storage.
     *
     * @throws ProtonException If the action cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws ProtonException;

    /**
     * Returns whether the application should exit after this command succeeds.
     * Commands continue the session unless they override this behavior.
     */
    public boolean isExit() {
        return false;
    }
}
