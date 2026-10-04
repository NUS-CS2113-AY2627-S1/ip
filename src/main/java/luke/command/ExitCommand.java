package luke.command;

import luke.storage.Storage;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Exits the application.
 */
public class ExitCommand extends Command {

    /**
     * Creates a command that exits Luke.
     */
    public ExitCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // No action is needed before Luke exits.
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
