package clammy.command;

import clammy.storage.Storage;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Displays a farewell and signals that the application should exit.
 */
public class ExitCommand extends Command {
    /**
     * Displays the farewell message without changing saved data.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Returns true to stop reading commands after this command executes.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
