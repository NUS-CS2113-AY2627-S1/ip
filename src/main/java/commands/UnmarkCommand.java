package commands;

import exception.SamSquareException;
import storage.Storage;
import tasks.Task;
import tasks.TaskList;
import ui.Ui;

/**
 * Marks a numbered task as not done, displays its status, and saves the updated list.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates an unmark command; the task list validates the number on execution.
     *
     * @param taskNumber One-based task number supplied by the user.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) throws SamSquareException {
        Task task = tasks.unmark(taskNumber);
        ui.showTaskStatus(task);
        Storage.save(tasks.getTasks());
    }
}
