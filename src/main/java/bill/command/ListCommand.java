package bill.command;

import bill.storage.Storage;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Displays every task in its current numbered order.
 */
public class ListCommand extends Command {
    /**
     * Creates a command that lists the tasks.
     */
    public ListCommand() {
    }

    /**
     * Displays the tasks in their current numbered order.
     *
     * @param tasks Task list to display.
     * @param ui User interface for the list.
     * @param storage Unused storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks);
    }
}
