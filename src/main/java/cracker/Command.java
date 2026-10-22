package cracker;

import java.util.List;

/**
 * Represents one action requested by the user.
 */
public abstract class Command {
    /**
     * Applies this command to the task list.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     * @param storage storage used to save changed tasks
     * @throws CrackerException if this command cannot be completed
     */
    public abstract void execute(List<Task> tasks, Ui ui, Storage storage) throws CrackerException;

    /**
     * Returns whether this command ends the chatbot session.
     *
     * @return whether this command is the exit command
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Returns a task selected by a one-based task number.
     *
     * @param tasks tasks available to select
     * @param taskNumberText text containing the one-based task number
     * @return selected task
     * @throws CrackerException if the list is empty or the number is invalid
     */
    protected Task getTask(List<Task> tasks, String taskNumberText) throws CrackerException {
        if (tasks.isEmpty()) {
            throw new CrackerException("There are no tasks yet. Add a task first.");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new CrackerException("Enter a task number from 1 to " + tasks.size() + ".");
            }
            return tasks.get(taskNumber - 1);
        } catch (NumberFormatException e) {
            throw new CrackerException("Enter a task number from 1 to " + tasks.size() + ".");
        }
    }
}
