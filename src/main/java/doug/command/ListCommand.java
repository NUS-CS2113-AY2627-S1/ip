package doug.command;

import doug.task.TaskList;
import doug.ui.Ui;
import doug.storage.Storage;
import doug.errors.DougException;

/**
 * Displays all tasks currently stored in the task list.
 */
public class ListCommand extends Command {
    /**
     * Displays the current task list.
     *
     * @param taskList task list to display
     * @param ui user interface used to display the tasks
     * @param storage storage supplied by the application
     * @throws DougException if the command cannot be completed
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DougException {
        ui.printTaskList(taskList.getTaskList());
    }
}
