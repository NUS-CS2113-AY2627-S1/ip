/** Marks the task at a given index as done. */
public class MarkCommand extends Command {
    private final int index;

    /** Creates a command that marks the task at {@code index} (zero-based) as done. */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ShaheerException {
        Task task = tasks.get(index);
        task.markAsDone();
        ui.showTaskMarked(task);
    }
}
