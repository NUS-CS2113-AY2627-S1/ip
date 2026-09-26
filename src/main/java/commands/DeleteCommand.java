package commands;

import exception.SamSquareException;
import storage.Storage;
import tasks.Task;
import tasks.TaskList;
import ui.Ui;

/**
 * Removes a numbered task, displays its confirmation, and saves the updated list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a deletion command; the task list validates the number on execution.
     *
     * @param taskNumber One-based task number supplied by the user.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) throws SamSquareException {
        Task task = tasks.delete(taskNumber);
        ui.showDeletedTask(task, tasks.size());
        Storage.save(tasks.getTasks());
    }
}
