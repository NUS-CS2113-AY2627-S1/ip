package dobby.command;

import dobby.storage.Storage;
import dobby.task.TaskList;
import dobby.ui.Ui;

/**
 * Represents the command that displays all stored tasks.
 */
public final class ListCommand extends Command {

    /** Creates a command that displays the task list. */
    public ListCommand() {
    }

    /**
     * Displays the current task list.
     *
     * @param tasks the task list to display.
     * @param ui the user interface used to display the tasks.
     * @param storage the storage handler, which is not needed for listing.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
