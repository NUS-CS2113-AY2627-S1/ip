/**
 * Adds one task to the list, whatever kind of task it is.
 * <p>
 * Todos, deadlines and events differ only in how their text is read, which is
 * {@link Parser}'s business. By the time the task reaches this command it is
 * already built, so one command serves all three.
 */
public class AddCommand extends Command {

    /** The task to add, already built from what the user typed. */
    private final Task task;

    /**
     * Constructs a command that adds the given task.
     *
     * @param task the task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CortisolException {
        tasks.add(task);
        ui.showAddedTask(task, tasks.size());
        storage.save(tasks.asArrayList());
    }
}
