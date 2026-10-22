package eric.command;

import eric.storage.Storage;
import eric.task.TaskList;
import eric.ui.Ui;

/** Shows all the tasks in the task list. */
public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getTasks());
    }
}
