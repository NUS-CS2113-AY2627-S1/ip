package buddy.command;

import buddy.storage.Storage;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Ends the program after saying goodbye.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
