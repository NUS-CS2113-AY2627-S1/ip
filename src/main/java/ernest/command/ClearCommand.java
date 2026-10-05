package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Removes every task from Ernest's task list.
 */
public final class ClearCommand extends Command {
    /**
     * Removes all tasks, saves the empty list, and displays confirmation.
     *
     * @param taskList task list to clear.
     * @param ui user interface through which confirmation is displayed.
     * @param storage storage to which the empty task list is saved.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        taskList.clearTasks();
        saveTasks(taskList, ui, storage);
        ui.showTaskListClearedMessage();
    }
}
