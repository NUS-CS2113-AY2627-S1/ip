package foodielover.command;

import foodielover.storage.Storage;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents a command to display all tasks in the task list.
 */
public class ListCommand extends Command {
    /**
     * Constructs a ListCommand.
     */
    public ListCommand() {
    }

    /**
     * Executes the list command by displaying all stored tasks.
     *
     * @param tasks Current task list.
     * @param ui User interface for user interactions.
     * @param storage Storage handler for persisting changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
