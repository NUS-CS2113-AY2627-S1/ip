package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Marks a task as incomplete.
 */
public class UnmarkCommand extends TaskIndexCommand {
    /**
     * Creates an unmark command.
     *
     * @param arguments task number entered by the user
     */
    public UnmarkCommand(String arguments) {
        super(arguments);
    }

    /**
     * Marks the selected task as incomplete and saves the updated list.
     *
     * @param tasks task list to update
     * @param ui user interface used for the response
     * @param storage storage used to persist the update
     * @throws DrPijonException when the task number is invalid or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        updateTaskStatus(tasks, ui, storage, false);
    }
}
