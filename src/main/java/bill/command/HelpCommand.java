package bill.command;

import bill.storage.Storage;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Displays the guide to supported commands.
 */
public class HelpCommand extends Command {
    /**
     * Creates a command that displays the help guide.
     */
    public HelpCommand() {
    }

    /**
     * Displays the available commands.
     *
     * @param tasks Unused task list.
     * @param ui User interface for the help text.
     * @param storage Unused storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showHelp();
    }
}
