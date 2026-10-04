package luke.command;

import luke.storage.Storage;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Shows every task in the task list.
 */
public class ListCommand extends Command {

    /**
     * Creates a command that lists all tasks.
     */
    public ListCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getTasks(), tasks.getTaskCount());
    }
}
