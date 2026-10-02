package slay69.command;

import java.io.IOException;

import slay69.Slay69Exception;
import slay69.storage.Storage;
import slay69.task.Task;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Removes a task and persists the updated task list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that deletes a task by its displayed number.
     *
     * @param taskNumber one-based task number
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Removes the selected task, confirms the change, and saves the list.
     *
     * @throws Slay69Exception if the task number is outside the list
     * @throws IOException if saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws Slay69Exception, IOException {
        Task removedTask = tasks.deleteTask(taskNumber);
        ui.showTaskDeleted(removedTask, tasks.size());
        storage.save(tasks.getTasks());
    }
}
