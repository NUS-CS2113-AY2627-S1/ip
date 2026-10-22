package doug.command;

import doug.task.Task;
import doug.task.TaskList;
import doug.storage.Storage;
import doug.errors.DougException;
import doug.ui.Ui;

/**
 * Marks or unmarks a task and saves the updated task list.
 */
public class MarkCommand extends Command {
    /** Zero-based index of the task whose completion state will be changed. */
    private int indexToMark;
    /** Completion state to assign to the selected task. */
    private boolean isMarked;

    /**
     * Creates a command that changes a task's completion state.
     *
     * @param index zero-based index of the task to update
     * @param isMarked completion state to assign to the task
     */
    public MarkCommand(int index, boolean isMarked) {
        this.indexToMark = index;
        this.isMarked = isMarked;
    }

    /**
     * Updates the task's completion state, displays the result, and saves the task list.
     *
     * @param taskList task list containing the task to update
     * @param ui user interface used to display the updated task
     * @param storage storage used to save the updated task list
     * @throws DougException if the task cannot be updated or the task list cannot be saved
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DougException {
        Task markedTask = taskList.markTask(indexToMark, isMarked);
        ui.printTaskMarked(markedTask, indexToMark + 1, isMarked);
        storage.save(taskList.getTaskList());
    }
}
