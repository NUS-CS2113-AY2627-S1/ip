package jamal.command;

import jamal.Storage;
import jamal.TaskList;
import jamal.Ui;

/** Shows every task in the list. */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
