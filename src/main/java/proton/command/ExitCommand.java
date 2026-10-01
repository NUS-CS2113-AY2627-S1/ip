package proton.command;

import proton.storage.Storage;
import proton.task.TaskList;
import proton.ui.Ui;

/**
 * Displays the farewell message and requests that the command loop stop.
 */
public class ExitCommand extends Command {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
