package nick.command;

import java.io.IOException;

import nick.storage.Storage;
import nick.task.Task;
import nick.task.TaskList;
import nick.ui.Ui;

/**
 * Adds a task (todo, deadline, or event) to the task list.
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

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        tasks.add(task);
        storage.save(tasks);
        ui.showAdded(task, tasks.size());
    }
}
