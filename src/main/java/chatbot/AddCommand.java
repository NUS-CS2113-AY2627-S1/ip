package chatbot;

import chatbot.task.Task;

/** Adds one task to the task list and saves the updated list. */
public class AddCommand extends Command {
    private final Task task;

    /** Creates an add command for the supplied task.
     *
     * @param task task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /** Adds the task, displays confirmation, and saves the updated list.
     *
     * @param tasks task list to update
     * @param ui interface used to display confirmation
     * @param storage storage used to save the updated list
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.save(tasks.asList());
    }
}
