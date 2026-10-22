package bill.command;

import bill.exception.BillException;
import bill.storage.Storage;
import bill.task.TaskList;
import bill.ui.Ui;

/**
 * Represents one parsed user instruction that Bill can execute.
 */
public abstract class Command {
    /**
     * Creates a command; only concrete command subclasses can be executed.
     */
    protected Command() {
    }

    /**
     * Performs this command using Bill's task list and its supporting services.
     *
     * @param tasks Task list to read or change.
     * @param ui User interface used for responses.
     * @param storage Storage used by commands that change tasks.
     * @throws BillException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws BillException;

    /**
     * Indicates whether this command ends the conversation.
     *
     * @return True only for the exit command.
     */
    public boolean isExit() {
        return false;
    }
}
