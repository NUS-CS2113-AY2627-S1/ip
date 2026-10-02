package foodielover.command;

import foodielover.FoodieloverException;
import foodielover.storage.Storage;
import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents a command to mark a task as completed.
 */
public class MarkCommand extends Command {
    /** Zero-based index of the task to mark. */
    private final int taskIndex;

    /**
     * Constructs a MarkCommand with the target task index.
     *
     * @param taskIndex Zero-based index of the task to mark.
     */
    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Executes the mark command by marking the task done, saving changes, and notifying the user.
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
                    "Your task list is empty. Please add a task before marking one.");
        }
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new FoodieloverException(
                    "This is not a valid task number. Please enter a number from 1 to " + tasks.size() + ".");
        }
        Task task = tasks.get(taskIndex);
        task.markAsDone();
        saveTasks(tasks, storage, ui);
        ui.showTaskMarked(task);
    }
}
