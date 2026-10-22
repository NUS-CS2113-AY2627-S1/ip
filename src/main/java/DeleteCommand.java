/**
 * Removes one task from the list.
 */
public class DeleteCommand extends Command {

    /** Number of the task as the user typed it, counting from 1. */
    private final int taskNumber;

    /**
     * Constructs a command that removes the given task.
     *
     * @param taskNumber number of the task, counting from 1.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CortisolException {
        // remove() returns the task it removed, so we can still report it
        // after it has left the list.
        Task removedTask = tasks.remove(taskNumber, "delete");

        ui.showRemovedTask(removedTask, tasks.size());
        storage.save(tasks.asArrayList());
    }
}
