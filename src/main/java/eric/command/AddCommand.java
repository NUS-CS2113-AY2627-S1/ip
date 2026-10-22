package eric.command;

import eric.exception.EricException;
import eric.storage.Storage;
import eric.task.Task;
import eric.task.TaskList;
import eric.ui.Ui;

/** Adds a task to the task list. */
public class AddCommand extends Command {
    /** An example of a command that adds a task, for messages that suggest what to type. */
    public static final String EXAMPLE_TODO = "todo read book";

    private final Task task;

    /**
     * Creates a command that adds {@code task}.
     *
     * @param task The task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EricException {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
