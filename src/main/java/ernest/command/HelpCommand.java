package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Displays guidance for Ernest's supported commands.
 */
public final class HelpCommand extends Command {
    /**
     * Displays guidance for Ernest's supported commands.
     *
     * @param taskList task list available to the command.
     * @param ui user interface through which the guidance is displayed.
     * @param storage storage available to the command.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showHelpMessage();
    }
}
