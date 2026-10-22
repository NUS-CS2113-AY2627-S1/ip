package bond.command;

import bond.exception.StorageException;
import bond.storage.Storage;
import bond.task.Task;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Adds a parsed task and saves the updated list before reporting success.
 */
public class AddCommand extends Command {

    private final Task task;

    /**
     * Creates an addition command for a task that has already been parsed.
     *
     * @param task Task to add to the session.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds and saves the task, removing the addition if saving fails.
     *
     * @param taskList Tasks available in the current session.
     * @param ui User interface used to confirm a successful addition.
     * @param storage File storage used to save the updated list.
     * @throws StorageException If the updated list cannot be saved.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws StorageException {
        taskList.addTask(task);
        try {
            storage.saveTasks(taskList);
        } catch (StorageException e) {
            taskList.removeLastTask();
            throw e;
        }
        ui.showTaskAdded(task, taskList.getSize());
    }
}
