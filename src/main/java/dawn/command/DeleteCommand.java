package dawn.command;

import dawn.exception.DawnException;
import dawn.parser.CommandWord;
import dawn.storage.Storage;
import dawn.task.Task;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/**
 * Represents a command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Constructs a command to delete a task at the specified index.
     *
     * @param taskIndex the zero-based index of the task to drop.
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Removes the task from the collection, saves the updated list, and displays a success message.
     *
     * @param tasks the current list of tasks.
     * @param ui the user interface component.
     * @param storage the file storage system.
     * @throws DawnException if the index is out of bounds or saving fails.
     */
    @Override
    public void execute(TaskList tasks, DawnUi ui, Storage storage) throws DawnException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new DawnException("I couldn't find that task number. Use: " + CommandWord.DELETE.usage());
        }
        Task removedTask = tasks.removeTask(taskIndex);
        try {
            storage.save(tasks);
        } catch (DawnException e) {
            tasks.insertTask(taskIndex, removedTask);
            throw e;
        }
        String taskCount = tasks.size() + (tasks.size() == 1 ? " task" : " tasks");
        ui.showMessage("Removed this task. You have " + taskCount + " left:\n\t"
                + removedTask + "\n\n");
    }
}
