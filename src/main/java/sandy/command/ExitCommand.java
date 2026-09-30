package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.storage.Storage;

/**
 * Ends the command loop.
 */
public class ExitCommand extends Command {
    /**
     * Displays the exit message.
     *
     * @param tasks The tasks managed by the application.
     * @param ui The console interface.
     * @param storage The task storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Returns that this command ends the command loop.
     *
     * @return {@code true}.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
