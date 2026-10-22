package isa.command;

import isa.exception.StorageException;
import isa.storage.Storage;
import isa.task.Task;
import isa.task.TaskList;
import isa.ui.Ui;

/**
 * Changes a task's completion status and saves the list.
 */
public class MarkCommand extends Command {
    private final int taskIndex;
    private final boolean isDone;

    /**
     * Creates a command to set a task's completion status.
     *
     * @param taskIndex Zero-based index of the task.
     * @param isDone Whether the task should be marked as done.
     */
    public MarkCommand(int taskIndex, boolean isDone) {
        this.taskIndex = taskIndex;
        this.isDone = isDone;
    }

    /**
     * Updates the task status, saves the list, and displays the result.
     *
     * @param tasks Current task list.
     * @param ui Console interface for the result.
     * @param storage Storage used to save the list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task task = tasks.get(taskIndex);

        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }

        try {
            storage.save(tasks);
        } catch (StorageException e) {
            ui.showSaveWarning(e.getMessage());
        }

        if (isDone) {
            ui.showMarked(task);
        } else {
            ui.showUnmarked(task);
        }
    }
}
