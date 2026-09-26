package moistbot.command;

import moistbot.exception.MoistBotException;
import moistbot.storage.Storage;
import moistbot.task.Task;
import moistbot.task.TaskManager;
import moistbot.ui.UserInterface;

/**
 * Provides the shared persistence and rollback workflow for completion-state commands.
 */
abstract class TaskCompletionCommand extends Command {
    private final int taskNumber;
    private final boolean isCompleted;
    private final String commandName;

    /**
     * Creates a command that changes one task's completion state.
     *
     * @param taskNumber The 1-based number of the task to change
     * @param isCompleted The new completion state
     * @param commandName The command name used in validation feedback
     */
    protected TaskCompletionCommand(int taskNumber, boolean isCompleted, String commandName) {
        this.taskNumber = taskNumber;
        this.isCompleted = isCompleted;
        this.commandName = commandName;
    }

    /**
     * Changes and saves a task's completion state, restoring it if saving fails.
     *
     * @param tasks The task list to modify
     * @param ui The user interface used to confirm the change
     * @param storage The persistence service used to save the updated list
     * @throws MoistBotException if the task number is invalid or the change cannot be saved
     */
    @Override
    public void execute(TaskManager tasks, UserInterface ui, Storage storage) throws MoistBotException {
        Task task = tasks.getExistingTask(taskNumber, commandName);
        boolean wasCompleted = task.isCompleted();
        task.setCompleted(isCompleted);
        try {
            storage.saveTasks(tasks);
        } catch (MoistBotException e) {
            task.setCompleted(wasCompleted);
            throw e;
        }
        showConfirmation(ui, task);
    }

    /**
     * Displays feedback appropriate to the concrete completion command.
     *
     * @param ui The user interface used for feedback
     * @param task The task whose completion state changed
     */
    protected abstract void showConfirmation(UserInterface ui, Task task);
}
