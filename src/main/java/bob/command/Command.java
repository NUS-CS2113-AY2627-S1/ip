package bob.command;

import bob.exception.EmptyError;
import bob.exception.IndexOutOfBoundsError;
import bob.exception.InvalidTaskNumberError;
import bob.exception.SavingError;
import bob.exception.SyntaxError;
import bob.exception.TaskNotFoundError;
import bob.storage.Storage;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Represents a user command that can be executed by the application.
 */
public abstract class Command {
    /**
     * Executes this command using the current tasks and user interface.
     *
     * @param tasks current tasks in display order.
     * @param ui interface used to display the result.
     * @param storage storage used by commands that change tasks.
     * @throws IndexOutOfBoundsError if a deletion targets an index outside the list.
     * @throws EmptyError if an addition has an empty description.
     * @throws InvalidTaskNumberError if a mark or unmark index is outside the list.
     * @throws SyntaxError if an addition has invalid syntax.
     * @throws TaskNotFoundError if no task description matches a search keyword.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws IndexOutOfBoundsError, EmptyError, SyntaxError, InvalidTaskNumberError, TaskNotFoundError;

    /**
     * Saves changed tasks and reports a failure while retaining the in-memory change.
     */
    protected void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks);
        } catch (SavingError e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Returns whether the application should stop after this command executes.
     */
    public boolean isExit() {
        return false;
    }
}
