package cracker;

import java.util.List;

/**
 * Represents a command that marks one task as completed.
 */
public class MarkCommand extends Command {
    /** Text containing the selected task number. */
    private final String taskNumberText;

    /**
     * Creates a command that marks the selected task as completed.
     *
     * @param taskNumberText text containing the selected task number
     */
    public MarkCommand(String taskNumberText) {
        this.taskNumberText = taskNumberText;
    }

    /**
     * Marks the selected task, saves the list, and displays confirmation.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     * @param storage storage used to save changed tasks
     * @throws CrackerException if the task number is invalid
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws CrackerException {
        Task task = getTask(tasks, taskNumberText);
        task.markAsDone();
        storage.saveTasks(tasks);
        ui.showTaskMarked(task);
    }
}
