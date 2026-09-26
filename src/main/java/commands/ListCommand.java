package commands;

import tasks.TaskList;
import ui.Ui;

/**
 * Displays the current tasks without changing their state or ending the session.
 */
public class ListCommand extends Command {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showTasks(tasks.getTasks());
    }
}
