package bob.command;

import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Displays the farewell message and requests that the application stop.
 */
public class ExitCommand extends Command {
    /**
     * Displays the farewell message without changing or saving tasks.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Returns true to stop the application after the farewell message.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
