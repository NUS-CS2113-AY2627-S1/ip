package echo;

/**
 * Marks the task at a given list position as not done.
 */
public class UnmarkCommand extends Command {
    /** 1-based position of the task to unmark, as typed by the user. */
    private final int taskNumber;

    /**
     * Creates a command that marks the task at the given position as not done.
     *
     * @param taskNumber 1-based position shown by the list command.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Marks the task as not done, confirms it to the user and saves the list.
     *
     * @throws EchoException if there is no task with this number.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EchoException {
        checkTaskNumber(tasks, taskNumber);
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        ui.showTaskStatusChanged(task, false);
        saveTasks(tasks, ui, storage);
    }
}
