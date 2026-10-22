package doug.command;

import doug.task.TaskList;
import doug.ui.Ui;
import doug.storage.Storage;
import doug.errors.DougException;

/**
 * Displays help information for the application's available commands.
 */
public class HelpCommand extends Command {
    /**
     * Displays the application's help information.
     *
     * @param taskList task list supplied by the application
     * @param ui user interface used to display help information
     * @param storage storage supplied by the application
     * @throws DougException if the command cannot be completed
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DougException {
        ui.printHelp();
    }
}
