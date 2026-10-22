package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /**
     * Creates an executable command.
     */
    protected Command() {
    }

    /**
     * Executes this command using the application's components.
     *
     * @param tasks task list to read or update
     * @param ui user interface used for responses
     * @param storage storage used to persist updates
     * @throws DrPijonException when command execution fails
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException;

    /**
     * Returns whether this command requests application exit.
     *
     * @return true when the command exits the application
     */
    public boolean isExit() {
        return false;
    }
}
