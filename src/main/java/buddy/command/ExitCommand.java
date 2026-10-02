package buddy.command;

import buddy.storage.Storage;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Ends the current Buddy session after showing a goodbye message.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Bye. Hope to see you again soon!");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
