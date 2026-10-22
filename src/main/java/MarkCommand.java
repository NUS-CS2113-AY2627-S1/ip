/**
 * Marks one task as done.
 */
public class MarkCommand extends Command {

    /** Number of the task as the user typed it, counting from 1. */
    private final int taskNumber;

    /**
     * Constructs a command that marks the given task as done.
     *
     * @param taskNumber number of the task, counting from 1.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CortisolException {
        Task task = tasks.get(taskNumber, "mark");
        task.markAsDone();

        ui.showMarkedTask(task);
        storage.save(tasks.asArrayList());
    }
}
