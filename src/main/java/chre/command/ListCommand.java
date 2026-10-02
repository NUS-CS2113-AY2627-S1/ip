package chre.command;

import chre.data.TaskList;
import chre.ui.Ui;
import chre.storage.Storage;
import chre.exception.ChreException;

/**
 * Displays all tasks in the task list.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command by displaying all tasks.
     *
     * @param taskList the task list to display
     * @param ui the UI for displaying output
     * @param storage the storage (unused for this command)
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws ChreException {
        ui.showTasks(taskList.getTasks());
    }
}
