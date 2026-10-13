package aglio.command;

import aglio.storage.Storage;
import aglio.task.TaskList;
import aglio.ui.Ui;

/** Represents the "list" command that displays all tasks. */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
