package wenwen.command;

import wenwen.exception.WenwenException;
import wenwen.storage.Storage;
import wenwen.task.Task;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Deletes a numbered task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that deletes the given one-based task number.
     *
     * @param taskNumber one-based task number
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WenwenException {
        Task removedTask = tasks.delete(taskNumber);
        storage.saveTasks(tasks.asList());
        ui.showTaskDeleted(removedTask, tasks.size());
    }
}
