package drpijon.command;

import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Exits the application after displaying its goodbye message.
 */
public class ExitCommand extends Command {
    /**
     * Creates an exit command.
     */
    public ExitCommand() {
    }

    /**
     * Displays the goodbye message.
     *
     * @param tasks task list, which is not modified
     * @param ui user interface used for the response
     * @param storage storage, which is not used
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Indicates that this command ends the application loop.
     *
     * @return true because this is the exit command
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
