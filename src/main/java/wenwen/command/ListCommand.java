package wenwen.command;

import wenwen.storage.Storage;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Displays every task in the task list.
 */
public class ListCommand extends Command {
    /**
     * Creates a command that displays the complete task list.
     */
    public ListCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
