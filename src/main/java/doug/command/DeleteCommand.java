package doug.command;

import doug.task.Task;
import doug.task.TaskList;
import doug.ui.Ui;
import doug.storage.Storage;
import doug.errors.DougException;

/**
 * Deletes a task from the task list and saves the updated list.
 */
public class DeleteCommand extends Command {
    /** Zero-based index of the task to delete. */
    private int indexToDelete;

    /**
     * Creates a command that deletes the task at the specified index.
     *
     * @param index zero-based index of the task to delete
     */
    public DeleteCommand(int index) {
        this.indexToDelete = index;
    }

    /**
     * Deletes the task, displays the result, and saves the task list.
     *
     * @param taskList task list from which the task is deleted
     * @param ui user interface used to display the deleted task
     * @param storage storage used to save the updated task list
     * @throws DougException if the task cannot be deleted or the updated list cannot be saved
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DougException {
        Task deletedTask = taskList.deleteTask(indexToDelete);
        ui.printTaskDeleted(deletedTask, indexToDelete + 1);
        storage.save(taskList.getTaskList());
    };
}
