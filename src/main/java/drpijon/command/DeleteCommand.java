package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Deletes a task at a specified one-based position.
 */
public class DeleteCommand extends TaskIndexCommand {
    /**
     * Creates a delete command.
     *
     * @param arguments task number entered by the user
     */
    public DeleteCommand(String arguments) {
        super(arguments);
    }

    /**
     * Deletes the selected task, saves the updated list, and displays the result.
     *
     * @param tasks task list to update
     * @param ui user interface used for the response
     * @param storage storage used to persist the update
     * @throws DrPijonException when the task number is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        Task deletedTask = tasks.remove(getTaskIndex(tasks));
        storage.save(tasks);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }
}
