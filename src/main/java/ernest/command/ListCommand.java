package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Displays all tasks in Ernest's task list.
 */
public final class ListCommand extends Command {
    /**
     * Displays every task in the task list.
     *
     * @param taskList task list to display.
     * @param ui user interface through which the tasks are displayed.
     * @param storage storage available to the command.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showTaskList(taskList.getTasks());
    }
}
