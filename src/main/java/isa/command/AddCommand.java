package isa.command;

import isa.exception.StorageException;
import isa.storage.Storage;
import isa.task.Task;
import isa.task.TaskList;
import isa.ui.Ui;

/**
 * Adds a task, saves the list, and acknowledges the addition.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command for the given task.
     *
     * @param task Task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task, saves the list, and displays the result.
     *
     * @param tasks Current task list.
     * @param ui Console interface for the result.
     * @param storage Storage used to save the list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);

        try {
            storage.save(tasks);
        } catch (StorageException e) {
            ui.showSaveWarning(e.getMessage());
        }

        ui.showAdded(task, tasks.size());
    }
}
