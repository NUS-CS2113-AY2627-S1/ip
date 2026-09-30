package sandy.command;

import java.io.IOException;

import sandy.TaskList;
import sandy.Ui;
import sandy.exception.SandyException;
import sandy.storage.Storage;

/**
 * Represents an operation that can be executed on the task list.
 */
public abstract class Command {
    /**
     * Executes this command using the application components.
     *
     * @param tasks The tasks managed by the application.
     * @param ui The console interface.
     * @param storage The task storage.
     * @throws SandyException If the command refers to an invalid task.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws SandyException;

    /**
     * Returns whether this command should stop the command loop.
     *
     * @return {@code true} when this is an exit command.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the task list and reports a write failure through the user interface.
     *
     * @param tasks The tasks to save.
     * @param ui The console interface used to report failures.
     * @param storage The task storage.
     */
    protected final void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasks(tasks.asList());
        } catch (IOException exception) {
            ui.showSaveError();
        }
    }
}
