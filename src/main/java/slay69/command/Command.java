package slay69.command;

import java.io.IOException;

import slay69.Slay69Exception;
import slay69.storage.Storage;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Represents a user command that can act on the application components.
 */
public abstract class Command {
    /**
     * Creates a command for a specific user action.
     */
    protected Command() {
    }

    /**
     * Executes this command.
     *
     * @param tasks task list on which the command operates
     * @param ui console interface used for feedback
     * @param storage persistence for changes to the task list
     * @throws Slay69Exception if the command cannot be applied to the task list
     * @throws IOException if a changed task list cannot be saved
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws Slay69Exception, IOException;

    /**
     * Returns whether this command should end the application.
     *
     * @return false unless overridden by an exit command
     */
    public boolean isExit() {
        return false;
    }
}
