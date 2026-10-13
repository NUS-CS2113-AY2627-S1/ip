package aglio.command;

import aglio.storage.Storage;
import aglio.task.TaskList;
import aglio.ui.Ui;

/**
 * Represents the "bye" command that signals the program to exit.
 * Prints the goodbye message, then returns true from {@link #isExit()}
 * so the main loop knows to stop.
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
