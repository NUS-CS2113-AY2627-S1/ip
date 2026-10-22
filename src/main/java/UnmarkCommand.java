/**
 * Marks one task as not done.
 */
public class UnmarkCommand extends Command {

    /** Number of the task as the user typed it, counting from 1. */
    private final int taskNumber;

    /**
     * Constructs a command that marks the given task as not done.
     *
     * @param taskNumber number of the task, counting from 1.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CortisolException {
        Task task = tasks.get(taskNumber, "unmark");
        task.markAsNotDone();

        ui.showUnmarkedTask(task);
        storage.save(tasks.asArrayList());
    }
}
