package proton.command;

import proton.storage.Storage;
import proton.task.TaskList;
import proton.ui.Ui;

/**
 * Displays the current tasks without changing or saving them.
 */
public class ListCommand extends Command {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks.asList());
    }
}
