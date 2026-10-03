package clammy.command;

import clammy.exception.TaskNotFoundException;
import clammy.storage.Storage;
import clammy.task.Task;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Marks a task as not done using its displayed number.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates an unmark command using a one-based task number.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks the numbered task as incomplete, displays confirmation, and saves the change.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TaskNotFoundException {
        Task task = tasks.getTask(taskNumber);
        task.markAsNotDone();
        ui.showTaskUnmarked(task);
        saveTasks(tasks, ui, storage);
    }
}
