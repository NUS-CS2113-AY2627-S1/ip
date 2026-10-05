package ernest.command;

import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Marks a numbered task as not done.
 */
public final class UnmarkCommand extends Command {
    /** One-based number of the task to unmark. */
    private final int taskNumber;

    /**
     * Creates a command that unmarks a specified task number.
     *
     * @param taskNumber one-based number of the task to unmark.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks the configured task as not done and reports the result.
     *
     * @param taskList task list containing the task to unmark.
     * @param ui user interface through which the result is displayed.
     * @param storage storage to which the updated task list is saved.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        TaskList.UnmarkStatus status = taskList.unmarkTask(taskNumber);
        switch (status) {
            case SUCCESS:
                saveTasks(taskList, ui, storage);
                ui.showTaskUnmarkedMessage(taskNumber);
                break;
            case INVALID_TASK_NUMBER:
                ui.showInvalidTaskNumberMessage();
                break;
            case ALREADY_NOT_DONE:
                ui.showTaskAlreadyNotDoneMessage(taskNumber);
                break;
        }
    }
}
