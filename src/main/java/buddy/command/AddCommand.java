package buddy.command;

import buddy.storage.Storage;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.ui.Ui;

/**
 * Adds a task (already constructed by the Parser) to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the given task.
     *
     * @param task the task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.showTaskAdded(task, tasks.size());
    }
}
