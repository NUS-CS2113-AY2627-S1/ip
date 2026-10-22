package chatbot;

import chatbot.exception.AgentCTException;
import chatbot.task.Task;

/** Deletes a numbered task and saves the updated list. */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /** Creates a delete command for a one-based task number.
     *
     * @param taskNumber one-based task number
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /** Deletes the selected task and saves the updated list.
     *
     * @param tasks task list to update
     * @param ui interface used to display confirmation
     * @param storage storage used to save the updated list
     * @throws AgentCTException when the task number is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AgentCTException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new AgentCTException("Sorry, that task number does not exist.");
        }
        Task deletedTask = tasks.remove(taskNumber - 1);
        storage.save(tasks.asList());
        ui.showTaskDeleted(deletedTask, tasks.size());
    }
}
