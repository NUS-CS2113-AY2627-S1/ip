package hedy.command;

import hedy.exception.HedyException;
import hedy.storage.Storage;
import hedy.task.Task;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Deletes one task by its list number and saves the list. */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that deletes a task.
     *
     * @param taskNumber number shown by {@code list}, starting at 1
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HedyException {
        Task removed = tasks.delete(taskNumber);
        storage.save(tasks.getTasks());
        ui.showDeleted(removed, tasks.size());
    }
}
