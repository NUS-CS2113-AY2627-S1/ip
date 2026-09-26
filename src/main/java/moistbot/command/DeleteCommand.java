package moistbot.command;

import moistbot.exception.MoistBotException;
import moistbot.storage.Storage;
import moistbot.task.Task;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

/**
 * Deletes a selected task from the task list.
 */
public final class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command for the supplied task number.
     *
     * @param taskNumber The 1-based number of the task to delete
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Deletes and saves a task, restoring it at its original position if saving fails.
     *
     * @param tasks The task list to modify
     * @param ui The user interface used to confirm the deletion
     * @param storage The persistence service used to save the updated list
     * @throws MoistBotException if the task number is invalid or the deletion cannot be saved
     */
    @Override
    public void execute(TaskManager tasks, UserInterface ui, Storage storage) throws MoistBotException {
        Task task = tasks.getExistingTask(taskNumber, "delete");
        tasks.deleteTask(taskNumber);
        try {
            storage.saveTasks(tasks);
        } catch (MoistBotException e) {
            tasks.restoreTask(taskNumber, task);
            throw e;
        }
        ui.printDeleteTask(task, tasks.getSize());
    }
}
