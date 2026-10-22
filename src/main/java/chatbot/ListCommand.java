package chatbot;

/** Displays all tasks in the task list. */
public class ListCommand extends Command {
    /** Displays all tasks in the task list.
     *
     * @param tasks task list to display
     * @param ui interface used to display the tasks
     * @param storage unused storage handler
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.asList());
    }
}
