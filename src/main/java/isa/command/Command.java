package isa.command;

import isa.exception.IsaException;
import isa.storage.Storage;
import isa.task.TaskList;
import isa.ui.Ui;

/**
 * Represents an action requested by the user.
 */
public abstract class Command {
    /**
     * Executes this command using the application's task list and services.
     *
     * @param tasks Current task list.
     * @param ui Console interface.
     * @param storage Task storage.
     * @throws IsaException If the command cannot be applied to the task list.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws IsaException;

    /**
     * Indicates whether this command ends the application.
     *
     * @return Whether the application should exit.
     */
    public boolean isExit() {
        return false;
    }
}
