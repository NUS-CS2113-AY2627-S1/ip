package cracker;

import java.util.List;

/**
 * Represents a command that removes one task.
 */
public class DeleteCommand extends Command {
    /** Text containing the selected task number. */
    private final String taskNumberText;

    /**
     * Creates a command that removes the selected task.
     *
     * @param taskNumberText text containing the selected task number
     */
    public DeleteCommand(String taskNumberText) {
        this.taskNumberText = taskNumberText;
    }

    /**
     * Removes the selected task, saves the list, and displays confirmation.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     * @param storage storage used to save changed tasks
     * @throws CrackerException if the task number is invalid
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws CrackerException {
        Task task = getTask(tasks, taskNumberText);
        tasks.remove(task);
        storage.saveTasks(tasks);
        ui.showTaskDeleted(task, tasks.size());
    }
}
