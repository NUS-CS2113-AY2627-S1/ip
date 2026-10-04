package dobby.command;

import dobby.exception.DobbyException;
import dobby.storage.Storage;
import dobby.task.Task;
import dobby.task.TaskList;
import dobby.ui.Ui;

/**
 * Represents the command that adds a new task.
 */
public final class AddCommand extends Command {

    /** The task to add. */
    private final Task task;

    /**
     * Creates a command that adds the supplied task.
     *
     * @param task the task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task, displays a confirmation, and saves the updated list.
     *
     * @param tasks the task list to update.
     * @param ui the user interface used to display the confirmation.
     * @param storage the storage handler used to persist the updated list.
     * @throws DobbyException when the task list is full.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DobbyException {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
