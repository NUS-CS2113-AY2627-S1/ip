package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.storage.Storage;
import sandy.task.Task;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the given task.
     *
     * @param task The task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task, displays confirmation, and saves the updated task list.
     *
     * @param tasks The tasks managed by the application.
     * @param ui The console interface.
     * @param storage The task storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.addTask(task);
        ui.showAddedTask(task, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
