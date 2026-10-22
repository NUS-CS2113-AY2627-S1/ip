package dawn.command;

import dawn.exception.DawnException;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/**
 * Represents a command to add a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Constructs a command to add the specified task.
     *
     * @param task the concrete task to add to the system.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Executes the task addition, updates storage, and displays a confirmation.
     *
     * @param tasks the current list of tasks.
     * @param ui the user interface component.
     * @param storage the file storage system.
     * @throws DawnException if the list capacity is exceeded or saving fails.
     */
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        tasks.addTask(task);
        try {
            storage.save(tasks);
        } catch (DawnException e) {
            tasks.removeTask(tasks.size() - 1);
            throw e;
        }
        ui.showMessage("Pip! Added this task: " + task.getDescription() + "\n\n");
    }
}
