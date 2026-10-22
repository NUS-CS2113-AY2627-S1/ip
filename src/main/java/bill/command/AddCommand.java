package bill.command;

import bill.exception.BillException;
import bill.storage.Storage;
import bill.task.Task;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Adds a parsed todo, deadline, or event to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates an add command for the supplied task.
     *
     * @param task Task created by the parser.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task, saves the updated list, and confirms the addition.
     *
     * @param tasks Task list to update.
     * @param ui User interface for the confirmation.
     * @param storage Storage used to persist the updated list.
     * @throws BillException If the updated list cannot be saved.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BillException {
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());
        ui.showAdded(task, tasks.size());
    }
}
