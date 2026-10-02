package foodielover.command;

import foodielover.FoodieloverException;
import foodielover.storage.Storage;
import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents a command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    /** Zero-based index of the task to delete. */
    private final int taskIndex;

    /**
     * Constructs a DeleteCommand with the target task index.
     *
     * @param taskIndex Zero-based index of the task to delete.
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Executes the delete command by removing the task, saving changes, and notifying the user.
     *
     * @param tasks Current task list.
     * @param ui User interface for user interactions.
     * @param storage Storage handler for persisting changes.
     * @throws FoodieloverException If the task index is out of bounds or the list is empty.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws FoodieloverException {
        if (tasks.isEmpty()) {
            throw new FoodieloverException(
                    "Your task list is empty. Please add a task before removing one.");
        }
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new FoodieloverException(
                    "This is not a valid task number. Please enter a number from 1 to " + tasks.size() + ".");
        }
        Task removedTask = tasks.remove(taskIndex);
        saveTasks(tasks, storage, ui);
        ui.showTaskRemoved(removedTask, tasks.size());
    }
}
