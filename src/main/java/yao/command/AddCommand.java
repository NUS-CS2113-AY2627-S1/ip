package yao.command;

import yao.Storage;
import yao.TaskList;
import yao.Ui;
import yao.YaoException;
import yao.task.Task;

/**
 * Command to add a new task to the task list and persist the change.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Constructs an AddCommand with the given task.
     *
     * @param task The task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws YaoException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
