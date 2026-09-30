package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.exception.SandyException;
import sandy.storage.Storage;
import sandy.task.Task;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that deletes the given task number.
     *
     * @param taskNumber The one-based task number.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Deletes the selected task, displays confirmation, and saves the updated list.
     *
     * @param tasks The tasks managed by the application.
     * @param ui The console interface.
     * @param storage The task storage.
     * @throws SandyException If the task number does not exist.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SandyException {
        Task deletedTask = tasks.deleteTask(taskNumber);
        ui.showDeletedTask(deletedTask, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
