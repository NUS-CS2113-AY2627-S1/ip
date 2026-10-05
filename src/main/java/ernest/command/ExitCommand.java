package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Ends the current Ernest session.
 */
public final class ExitCommand extends Command {
    /**
     * Displays Ernest's farewell message.
     *
     * @param taskList task list available to the command.
     * @param ui user interface through which the message is displayed.
     * @param storage storage available to the command.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showGoodbyeMessage();
    }

    /**
     * Returns whether this command exits Ernest.
     *
     * @return true because this is an exit command.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
