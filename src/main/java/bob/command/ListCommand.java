package bob.command;

import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Displays the current task list without changing it.
 */
public class ListCommand extends Command {
    /**
     * Displays all current tasks in their existing order without changing or saving them.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
