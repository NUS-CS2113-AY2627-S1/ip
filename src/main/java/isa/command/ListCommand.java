package isa.command;

import isa.storage.Storage;
import isa.task.TaskList;
import isa.ui.Ui;

/**
 * Displays the tasks in their current order.
 */
public class ListCommand extends Command {
    /**
     * Displays all tasks in their current order.
     *
     * @param tasks Current task list.
     * @param ui Console interface for the list.
     * @param storage Task storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks);
    }
}
