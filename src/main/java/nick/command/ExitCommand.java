package nick.command;

import nick.storage.Storage;
import nick.task.TaskList;
import nick.ui.Ui;

/**
 * Ends the program after showing the farewell message.
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
