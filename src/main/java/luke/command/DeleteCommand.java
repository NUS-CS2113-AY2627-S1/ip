package luke.command;

import java.io.IOException;

import luke.exception.LukeException;
import luke.storage.Storage;
import luke.task.Task;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that deletes the specified task.
     *
     * @param taskNumber One-based number of the task to delete.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws LukeException, IOException {
        Task deletedTask = tasks.delete(taskNumber);
        storage.save(tasks.getTasks(), tasks.getTaskCount());
        ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
    }
}
