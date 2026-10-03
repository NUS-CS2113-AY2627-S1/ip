package clammy.command;

import java.io.IOException;

import clammy.exception.ClammyException;
import clammy.storage.Storage;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Represents a user action with parsed arguments, ready to execute.
 */
public abstract class Command {
    /**
     * Executes this command and displays its result.
     *
     * @param tasks Current tasks in their displayed order.
     * @param ui User interface used to display the result.
     * @param storage Storage used to save task changes.
     * @throws ClammyException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws ClammyException;

    /**
     * Returns whether the application should exit after this command succeeds.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves a successful change, warning the user if it remains in memory only.
     */
    protected void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks);
        } catch (IOException exception) {
            ui.showSavingError();
        }
    }
}
