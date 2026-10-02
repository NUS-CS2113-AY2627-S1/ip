package chre.command;

import chre.data.TaskList;
import chre.ui.Ui;
import chre.storage.Storage;

/**
 * Exits the application.
 */
public class ByeCommand extends Command {
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        // No action needed; exit is handled by the isExit() method
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
