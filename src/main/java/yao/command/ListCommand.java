package yao.command;

import yao.Storage;
import yao.TaskList;
import yao.Ui;

/**
 * Command to display all tasks in the task list.
 */
public class ListCommand extends Command {

    /**
     * Constructs a ListCommand instance.
     */
    public ListCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
