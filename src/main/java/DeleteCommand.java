/** Removes the task at a given index from the task list. */
public class DeleteCommand extends Command {
    private final int index;

    /** Creates a command that deletes the task at {@code index} (zero-based). */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ShaheerException {
        Task task = tasks.delete(index);
        ui.showTaskDeleted(task, tasks.size());
    }
}
