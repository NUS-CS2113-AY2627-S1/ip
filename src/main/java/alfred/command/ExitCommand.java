package alfred.command;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;

/**
 * Displays Alfred's farewell and ends the application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbyeMessage();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
