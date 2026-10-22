package chatbot;

import chatbot.exception.AgentCTException;

/** Marks a numbered task as done and saves the updated list. */
public class MarkCommand extends Command {
    private final int taskNumber;

    /** Creates a mark command for a one-based task number.
     *
     * @param taskNumber one-based task number
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /** Marks the selected task as done and saves the updated list.
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
        int taskIndex = taskNumber - 1;
        tasks.get(taskIndex).markAsDone();
        storage.save(tasks.asList());
        ui.showTaskMarked(tasks.get(taskIndex));
    }
}
