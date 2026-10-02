package alfred.command;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;

/**
 * Displays the available commands and their expected formats.
 */
public class HelpCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showHelp();
    }
}
