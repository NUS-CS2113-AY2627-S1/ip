package ernest.command;

import ernest.storage.Storage;
import ernest.task.Task;
import ernest.task.TaskList;
import ernest.ui.Ui;

import java.util.Optional;

/**
 * Deletes a numbered task from Ernest's task list.
 */
public final class DeleteCommand extends Command {
    /** One-based number of the task to delete. */
    private final int taskNumber;

    /**
     * Creates a command that deletes a specified task number.
     *
     * @param taskNumber one-based number of the task to delete.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Deletes the configured task and reports whether its number is valid.
     *
     * @param taskList task list from which the task is deleted.
     * @param ui user interface through which the result is displayed.
     * @param storage storage to which the updated task list is saved.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        Optional<Task> deletedTask = taskList.deleteTask(taskNumber);
        if (deletedTask.isPresent()) {
            saveTasks(taskList, ui, storage);
            ui.showTaskDeletedMessage(deletedTask.get(), taskList.getTaskCount(),
                    taskList.getMaximumTaskCount());
        } else {
            ui.showInvalidTaskNumberMessage();
        }
    }
}
