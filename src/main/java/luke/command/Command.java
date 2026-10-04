package luke.command;

import java.io.IOException;

import luke.exception.LukeException;
import luke.storage.Storage;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Represents an instruction that Luke can execute.
 */
public abstract class Command {

    /**
     * Creates a command.
     */
    protected Command() {
    }

    /**
     * Executes this command using the application components.
     *
     * @param tasks Task list to query or modify.
     * @param ui User interface used to show results.
     * @param storage Storage used to persist changes.
     * @throws LukeException If the command cannot be completed.
     * @throws IOException If changed tasks cannot be saved.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws LukeException, IOException;

    /**
     * Returns whether this command exits the application.
     *
     * @return True if Luke should exit after this command.
     */
    public boolean isExit() {
        return false;
    }
}
