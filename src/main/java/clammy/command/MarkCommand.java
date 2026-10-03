package clammy.command;

import clammy.exception.TaskNotFoundException;
import clammy.storage.Storage;
import clammy.task.Task;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Marks a task as done using its displayed number.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a mark command using a one-based task number.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks the numbered task as done, displays confirmation, and saves the change.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws TaskNotFoundException {
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        ui.showTaskMarked(task);
        saveTasks(tasks, ui, storage);
    }
}
