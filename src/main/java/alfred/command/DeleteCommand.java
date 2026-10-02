package alfred.command;

import java.io.IOException;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;
import alfred.exception.AlfredException;
import alfred.task.Task;

/**
 * Deletes a task from Alfred's task list.
 */
public class DeleteCommand extends Command {

    private final int taskNumber;

    /**
     * Creates a command that deletes the task with the specified one-based number.
     *
     * @param taskNumber One-based number of the task to delete.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AlfredException, IOException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AlfredException("Invalid task number.");
        }

        Task deletedTask = tasks.delete(taskNumber - 1);
        storage.save(tasks);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }
}
