package wenwen.command;

import wenwen.exception.WenwenException;
import wenwen.storage.Storage;
import wenwen.task.Task;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the supplied task.
     *
     * @param task task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws WenwenException {
        tasks.add(task);
        storage.saveTasks(tasks.asList());
        ui.showTaskAdded(task, tasks.size());
    }
}
