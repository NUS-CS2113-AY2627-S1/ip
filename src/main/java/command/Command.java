package command;

import display.Ui;
import exception.AtlasException;
import list.TaskList;
import storage.Storage;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /**
     * Executes the command against the provided task list, user interface, and storage.
     *
     * @param tasks The active TaskList to operate on.
     * @param ui The Ui instance used to display output to the user.
     * @param storage The Storage instance used to save or load tasks.
     * @throws AtlasException If an execution error occurs.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws AtlasException;

    /**
     * Indicates whether this command signals the application to terminate.
     *
     * @return True if the application should exit; false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}