package bryte.command;

import bryte.Storage;
import bryte.TaskList;
import bryte.Ui;

/**
 * Represents a command to exit the application.
 */
public class ExitCommand extends Command {
    /**
     * Constructs a new {@code ExitCommand}.
     */
    public ExitCommand() {
    }

    /**
     * Executes the exit command (no action needed).
     *
     * @param tasks The task list.
     * @param ui The user interface.
     * @param storage The storage for saving tasks.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // No execution needed for exit
    }

    /**
     * Indicates that this command terminates the application.
     *
     * @return True since this is an exit command.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
