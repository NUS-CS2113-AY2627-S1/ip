package foodielover.command;

import foodielover.storage.Storage;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents a command to exit the application.
 */
public class ExitCommand extends Command {
    /**
     * Constructs an ExitCommand.
     */
    public ExitCommand() {
    }

    /**
     * Executes the exit command by displaying the goodbye message.
     *
     * @param tasks Current task list.
     * @param ui User interface for user interactions.
     * @param storage Storage handler for persisting changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Indicates that this command terminates the application.
     *
     * @return True.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
