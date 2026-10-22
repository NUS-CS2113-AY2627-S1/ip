package drpijon.command;

import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Displays all tasks in the task list.
 */
public class ListCommand extends Command {
    /**
     * Creates a list command.
     */
    public ListCommand() {
    }

    /**
     * Displays every task in the task list.
     *
     * @param tasks task list to display
     * @param ui user interface used for the response
     * @param storage storage, which is not modified
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
