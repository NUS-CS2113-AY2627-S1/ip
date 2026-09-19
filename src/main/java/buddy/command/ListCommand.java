package buddy.command;

import buddy.storage.Storage;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Shows every task currently in the task list.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getAll());
    }
}
