package alfred.command;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;

/**
 * Displays all tasks known to Alfred.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
