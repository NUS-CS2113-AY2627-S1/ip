package foodielover.command;

import foodielover.FoodieloverException;
import foodielover.storage.Storage;
import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents a command to mark a task as not completed.
 */
public class UnmarkCommand extends Command {
    /** Zero-based index of the task to unmark. */
    private final int taskIndex;

    /**
     * Constructs an UnmarkCommand with the target task index.
     *
     * @param taskIndex Zero-based index of the task to unmark.
     */
    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Executes the unmark command by marking the task undone, saving changes, and notifying the user.
     *
     * @param tasks Current task list.
     * @param ui User interface for user interactions.
     * @param storage Storage handler for persisting changes.
     * @throws FoodieloverException If the task index is out of bounds.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws FoodieloverException {
        if (tasks.isEmpty()) {
            throw new FoodieloverException(
                    "Your task list is empty. Please add a task before unmarking one.");
        }
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new FoodieloverException(
                    "This is not a valid task number. Please enter a number from 1 to " + tasks.size() + ".");
        }
        Task task = tasks.get(taskIndex);
        task.markAsUndone();
        saveTasks(tasks, storage, ui);
        ui.showTaskUnmarked(task);
    }
}
