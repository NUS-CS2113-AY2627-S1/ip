package slay69.command;

import slay69.storage.Storage;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Displays all tasks without changing the task list.
 */
public class ListCommand extends Command {
    /**
     * Creates a command that lists all tasks.
     */
    public ListCommand() {
    }

    /**
     * Displays all tasks without modifying or saving them.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks.getTasks());
    }
}
