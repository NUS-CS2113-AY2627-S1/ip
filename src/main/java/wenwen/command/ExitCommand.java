package wenwen.command;

import wenwen.storage.Storage;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Ends the current Wenwen session.
 */
public class ExitCommand extends Command {
    /**
     * Creates a command that ends the application.
     */
    public ExitCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showFarewell();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
