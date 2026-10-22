package aigis;

/**
 * Shares execution logic for commands that change a task's completion status.
 */
abstract class TaskStatusCommand extends Command {
    private final int taskNumber;
    private final boolean isDone;
    private final String statusMessage;

    /**
     * Creates a task-status command.
     *
     * @param taskNumber The one-based number of the target task.
     * @param isDone The completion state to apply.
     * @param statusMessage The message prefix shown after the update.
     */
    protected TaskStatusCommand(int taskNumber, boolean isDone, String statusMessage) {
        this.taskNumber = taskNumber;
        this.isDone = isDone;
        this.statusMessage = statusMessage;
    }

    /**
     * Updates the selected task when it exists.
     *
     * @param tasks The task list being changed.
     * @param ui The UI used to display the result.
     * @param storage The storage service, which saves when the application exits.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            ui.showMessage("That task does not exist.");
            return;
        }
        tasks.get(taskIndex).setDone(isDone);
        ui.showMessage(statusMessage + tasks.get(taskIndex));
    }
}
