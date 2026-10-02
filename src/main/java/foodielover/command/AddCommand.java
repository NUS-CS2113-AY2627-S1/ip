package foodielover.command;

import foodielover.storage.Storage;
import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents a command to add a task to the task list.
 */
public class AddCommand extends Command {
    /** Task to be added. */
    private final Task task;

    /**
     * Constructs an AddCommand with the task to be added.
     *
     * @param task Task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Executes the add command by adding the task, saving to storage, and notifying the user.
     *
     * @param tasks Current task list.
     * @param ui User interface for user interactions.
     * @param storage Storage handler for persisting changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        saveTasks(tasks, storage, ui);
        ui.showTaskAdded(task, tasks.size());
    }
}
