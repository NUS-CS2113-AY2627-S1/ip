package bryte.command;

import bryte.Storage;
import bryte.TaskList;
import bryte.Ui;

/**
 * Represents a command to list all tasks.
 */
public class ListCommand extends Command {
    /**
     * Constructs a new {@code ListCommand}.
     */
    public ListCommand() {
    }

    /**
     * Executes the list command by displaying all tasks in the task list.
     *
     * @param tasks The task list.
     * @param ui The user interface.
     * @param storage The storage for saving tasks.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showDivider();
        ui.showMessage(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage(" " + (i + 1) + "." + tasks.get(i).toString());
        }
        ui.showDivider();
    }
}
