package clammy.command;

import clammy.exception.TaskNotFoundException;
import clammy.storage.Storage;
import clammy.task.Task;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Removes a task identified by its displayed number.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a deletion command using a one-based task number.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Removes the numbered task, displays confirmation, and saves the updated list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TaskNotFoundException {
        Task task = tasks.removeTask(taskNumber);
        ui.showTaskDeleted(task, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
