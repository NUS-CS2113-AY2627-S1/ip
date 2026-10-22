package bond.command;

import bond.storage.Storage;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Displays the session's tasks in their stored order without changing them.
 */
public class ListCommand extends Command {

    /**
     * Displays all tasks in their stored order without changing them.
     *
     * @param taskList Tasks to display.
     * @param ui User interface used to display the tasks.
     * @param storage File storage, which this command does not use.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showTaskList(taskList);
    }
}
