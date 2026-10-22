package bob.command;

import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Ignores blank input without displaying a message or changing tasks.
 */
public class EmptyCommand extends Command {
    /**
     * Ignores blank input without changing tasks, saving, or displaying a message.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
    }
}
