/** Adds a todo, deadline or event to the task list. */
public class AddCommand extends Command {
    private final Task task;

    /** Creates a command that adds {@code task} to the list. */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }
}
