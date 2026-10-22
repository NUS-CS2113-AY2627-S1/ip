package bond.command;

import bond.storage.Storage;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Displays the farewell message and requests the end of the console session.
 */
public class ExitCommand extends Command {

    /**
     * Displays the farewell message without changing tasks or storage.
     *
     * @param taskList Tasks in the current session, which this command does not use.
     * @param ui User interface used to display the farewell message.
     * @param storage File storage, which this command does not use.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showGoodbyeMessage();
    }

    /**
     * Returns whether the session should end after this command executes.
     *
     * @return True because this command exits the session.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
