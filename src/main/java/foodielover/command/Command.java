package foodielover.command;

import java.io.IOException;

import foodielover.FoodieloverException;
import foodielover.storage.Storage;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /**
     * Constructs a Command instance.
     */
    protected Command() {
    }

    /**
     * Executes the command using the provided task list, user interface, and storage.
     *
     * @param tasks Current task list.
     * @param ui User interface for user interactions.
     * @param storage Storage handler for persisting changes.
     * @throws FoodieloverException If a command execution error occurs.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws FoodieloverException;

    /**
     * Indicates whether this command causes the application to terminate.
     *
     * @return True if the application should terminate, false otherwise.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the current task list to storage and displays an error message if saving fails.
     *
     * @param tasks Task list to persist.
     * @param storage Storage handler.
     * @param ui User interface for error reporting.
     */
    protected void saveTasks(TaskList tasks, Storage storage, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException exception) {
            ui.showSaveError(exception.getMessage());
        }
    }
}
