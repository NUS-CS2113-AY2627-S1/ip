package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Reports a command that Ernest does not recognize.
 */
public final class InvalidCommand extends Command {
    /**
     * Displays a message explaining that the command is invalid.
     *
     * @param taskList task list available to the command.
     * @param ui user interface through which the message is displayed.
     * @param storage storage available to the command.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showInvalidCommandMessage();
    }
}
