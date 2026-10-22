package bill.command;

import bill.exception.BillException;
import bill.storage.Storage;
import bill.task.Task;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Marks a numbered task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates an unmark command for the displayed task number.
     *
     * @param taskNumber One-based task number.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks the numbered task incomplete, saves it, and confirms the change.
     *
     * @param tasks Task list to update.
     * @param ui User interface for the confirmation.
     * @param storage Storage used to persist the updated list.
     * @throws BillException If the task number is invalid or the list cannot be saved.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BillException {
        Task task = tasks.unmark(taskNumber);
        storage.saveTasks(tasks.getTasks());
        ui.showUnmarked(task);
    }
}
