package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.storage.Storage;

/**
 * Displays all tasks in the task list.
 */
public class ListCommand extends Command {
    /**
     * Displays all tasks in the task list.
     *
     * @param tasks The tasks to display.
     * @param ui The console interface.
     * @param storage Unused because listing does not modify task data.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
