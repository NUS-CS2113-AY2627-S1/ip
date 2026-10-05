package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Represents a user command that Ernest can execute.
 */
public abstract class Command {
    /**
     * Executes this command using Ernest's application components.
     *
     * @param taskList task list on which the command can operate.
     * @param ui user interface through which the command can respond.
     * @param storage storage through which the command can persist changes.
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage);

    /**
     * Returns whether this command exits Ernest.
     *
     * @return true if the command exits Ernest; otherwise false.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the task list and displays a warning if saving fails.
     *
     * @param taskList task list to save.
     * @param ui user interface through which to display a warning.
     * @param storage storage to which the tasks are saved.
     */
    protected final void saveTasks(TaskList taskList, Ui ui, Storage storage) {
        if (!storage.saveTasks(taskList.getTasks())) {
            ui.showSaveErrorMessage();
        }
    }
}
