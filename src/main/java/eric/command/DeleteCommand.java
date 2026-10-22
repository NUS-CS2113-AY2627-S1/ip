package eric.command;

import eric.exception.EricException;
import eric.storage.Storage;
import eric.task.Task;
import eric.task.TaskList;
import eric.ui.Ui;

/** Deletes a task from the task list. */
public class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";

    private final int taskNumber;

    /**
     * Creates a command that deletes a task.
     *
     * @param taskNumber Number of the task to delete, as shown by the list command (the first is 1).
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EricException {
        int index = findTaskIndex(tasks, taskNumber, COMMAND_WORD);
        Task removedTask = tasks.remove(index);
        ui.showTaskRemoved(removedTask, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
