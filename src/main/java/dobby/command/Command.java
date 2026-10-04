package dobby.command;

import java.io.IOException;

import dobby.exception.DobbyException;
import dobby.storage.Storage;
import dobby.task.TaskList;
import dobby.ui.Ui;

/**
 * Represents an action that Dobby can execute.
 */
public abstract class Command {

    /** Creates a command. */
    protected Command() {
    }

    /**
     * Executes this command using the supplied application components.
     *
     * @param tasks the task list managed by Dobby.
     * @param ui the user interface used to display command responses.
     * @param storage the storage handler used to persist task changes.
     * @throws DobbyException when the command cannot be executed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws DobbyException;

    /**
     * Returns whether this command ends the Dobby session.
     *
     * @return true when this command requests application exit.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the current task list and reports storage failures through the user interface.
     *
     * @param tasks the task list to save.
     * @param ui the user interface used to report a save failure.
     * @param storage the storage handler used to save the tasks.
     */
    protected void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasks(tasks);
        } catch (IOException exception) {
            ui.showSaveError();
        }
    }
}
