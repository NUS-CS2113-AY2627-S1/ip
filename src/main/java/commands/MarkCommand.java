package commands;

import exception.SamSquareException;
import storage.Storage;
import tasks.Task;
import tasks.TaskList;
import ui.Ui;

/**
 * Marks a numbered task as done, displays its status, and saves the updated list.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a mark command; the task list validates the number on execution.
     *
     * @param taskNumber One-based task number supplied by the user.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) throws SamSquareException {
        Task task = tasks.mark(taskNumber);
        ui.showTaskStatus(task);
        Storage.save(tasks.getTasks());
    }
}
