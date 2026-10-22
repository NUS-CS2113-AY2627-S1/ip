package bill.command;

import bill.storage.Storage;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Signals that Bill should stop accepting commands.
 */
public class ExitCommand extends Command {
    /**
     * Creates a command that ends the conversation.
     */
    public ExitCommand() {
    }

    /**
     * Does nothing; the application loop checks {@link #isExit()} to stop.
     *
     * @param tasks Unused task list.
     * @param ui Unused user interface.
     * @param storage Unused storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // Bill prints the farewell after the command loop ends.
    }

    /**
     * Identifies this command as the one that ends the conversation.
     *
     * @return Always true.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
