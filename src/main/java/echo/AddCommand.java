package echo;

/**
 * Adds a task (to-do, deadline or event) to the task list.
 */
public class AddCommand extends Command {
    /** The task to add, already built from the user's input. */
    private final Task task;

    /**
     * Creates a command that adds the given task.
     *
     * @param task the already-parsed task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /** Adds the task to the list, confirms it to the user and saves the list. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
