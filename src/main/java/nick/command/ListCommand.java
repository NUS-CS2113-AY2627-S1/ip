package nick.command;

import nick.storage.Storage;
import nick.task.TaskList;
import nick.ui.Ui;

/**
 * Lists all tasks in the task list.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks);
    }
}
