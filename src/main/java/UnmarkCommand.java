/** Marks the task at a given index as not done. */
public class UnmarkCommand extends Command {
    private final int index;

    /** Creates a command that marks the task at {@code index} (zero-based) as not done. */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ShaheerException {
        Task task = tasks.get(index);
        task.markAsNotDone();
        ui.showTaskUnmarked(task);
    }
}
