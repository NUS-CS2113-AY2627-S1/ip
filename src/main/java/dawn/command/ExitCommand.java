package dawn.command;

import dawn.storage.Storage;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/**
 * Represents a command to exit the application.
 */
public class ExitCommand extends Command {
    /**
     * Executes the exit command. No action is required as the main loop will terminate.
     *
     * @param tasks the current list of tasks.
     * @param ui the user interface component.
     * @param storage the file storage system.
     */
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) {
        // No execution action needed for exit; Dawn handles bye presentation
    }

    /**
     * Indicates that this command terminates the application loop.
     *
     * @return {@code true}.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
