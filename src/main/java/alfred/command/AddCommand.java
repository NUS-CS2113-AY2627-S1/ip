package alfred.command;

import java.io.IOException;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;
import alfred.task.Task;

/**
 * Adds a task to Alfred's task list.
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
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
