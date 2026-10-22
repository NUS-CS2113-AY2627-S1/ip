package doug.command;

import doug.task.Task;
import doug.task.TaskList;
import doug.ui.Ui;
import doug.storage.Storage;

/**
 * Adds a task to the task list and saves the updated list.
 */
public class AddCommand extends Command {
    /** The task that will be added when this command is executed. */
    private Task taskToAdd;

    /**
     * Creates a command that adds the specified task.
     *
     * @param task task to add to the task list
     */
    public AddCommand(Task task) {
        this.taskToAdd = task;
    }

    /**
     * Adds the task, displays the updated task count, and saves the task list.
     *
     * @param taskList task list to which the task is added
     * @param ui user interface used to display the result
     * @param storage storage used to save the updated task list
     * @throws doug.errors.DougException if the updated task list cannot be saved
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws doug.errors.DougException {
        taskList.addTask(taskToAdd);
        ui.printTaskAdded(taskToAdd, taskList.size());
        storage.save(taskList.getTaskList());
    };
}
