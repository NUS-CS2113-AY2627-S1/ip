package commands;

import tasks.TaskList;
import ui.Ui;

/**
 * Displays the farewell response and requests the end of the console session.
 */
public class ExitCommand extends Command {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showGoodbye();
    }

    /**
     * Indicates that the application should stop after the farewell response.
     *
     * @return True because this command ends the session.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
