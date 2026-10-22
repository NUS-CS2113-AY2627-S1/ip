package yao.command;

import yao.Storage;
import yao.TaskList;
import yao.Ui;
import yao.YaoException;

/**
 * Represents an abstract executable user command.
 * All specific user commands inherit from this class.
 */
public abstract class Command {

    /**
     * Constructs a Command instance.
     */
    public Command() {
    }

    /**
     * Executes the command using the provided task list, user interface, and storage.
     *
     * @param tasks The active task list.
     * @param ui The user interface for displaying feedback.
     * @param storage The storage handler for saving tasks.
     * @throws YaoException If a domain error occurs during execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws YaoException;

    /**
     * Determines whether this command signals the application to terminate.
     *
     * @return true if this command exits the application; false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
