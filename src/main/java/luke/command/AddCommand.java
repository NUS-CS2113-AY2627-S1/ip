package luke.command;

import java.io.IOException;

import luke.storage.Storage;
import luke.task.Task;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the specified task.
     *
     * @param task Task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        tasks.add(task);
        storage.save(tasks.getTasks(), tasks.getTaskCount());
        ui.showTaskAdded(task, tasks.getTaskCount());
    }
}
