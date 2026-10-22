package cracker;

import java.util.List;

/**
 * Represents a command that adds one task to the task list.
 */
public abstract class AddCommand extends Command {
    /** Task that this command adds. */
    private final Task task;

    /**
     * Creates a command that adds the supplied task.
     *
     * @param task task to add
     */
    protected AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds this command's task, saves the list, and displays confirmation.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     * @param storage storage used to save changed tasks
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.saveTasks(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
