package bill.command;

import bill.exception.BillException;
import bill.storage.Storage;
import bill.task.Task;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Removes a numbered task from the list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a delete command for the displayed task number.
     *
     * @param taskNumber One-based task number.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Deletes the numbered task, saves the list, and confirms the deletion.
     *
     * @param tasks Task list to update.
     * @param ui User interface for the confirmation.
     * @param storage Storage used to persist the updated list.
     * @throws BillException If the task number is invalid or the list cannot be saved.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BillException {
        Task removedTask = tasks.delete(taskNumber);
        storage.saveTasks(tasks.getTasks());
        ui.showDeleted(removedTask, tasks.size());
    }
}
