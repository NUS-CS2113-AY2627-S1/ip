package chre.command;

import chre.data.TaskList;
import chre.ui.Ui;
import chre.storage.Storage;
import chre.exception.ChreException;
import chre.task.Task;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private int index;

    /**
     * Creates a DeleteCommand for the given task index.
     *
     * @param index the task index (1-based)
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws ChreException {
        if (taskList.size() == 0) {
            throw new ChreException("You don't have any tasks yet! Would you like to add one?");
        }
        if (index <= 0 || index > taskList.size()) {
            throw new ChreException("I couldn't find that task. Could you check the task number and try again?");
        }
        Task deletedTask = taskList.deleteTask(index);
        ui.showTaskDeleted(deletedTask, taskList.size());
        storage.save(taskList.getTasks());
    }
}
