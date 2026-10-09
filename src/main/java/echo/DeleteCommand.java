package echo;

/**
 * Removes the task at a given list position.
 */
public class DeleteCommand extends Command {
    /** 1-based position of the task to remove, as typed by the user. */
    private final int taskNumber;

    /**
     * Creates a command that removes the task at the given position.
     *
     * @param taskNumber 1-based position shown by the list command.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Removes the task, confirms it to the user with the remaining count and
     * saves the list.
     *
     * @throws EchoException if there is no task with this number.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EchoException {
        checkTaskNumber(tasks, taskNumber);
        Task removed = tasks.remove(taskNumber - 1);
        ui.showTaskDeleted(removed, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
