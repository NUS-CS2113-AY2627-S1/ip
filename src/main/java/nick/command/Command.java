package nick.command;

import java.io.IOException;

import nick.NickException;
import nick.storage.Storage;
import nick.task.TaskList;
import nick.ui.Ui;

/**
 * Represents a user command that can be executed against the task list.
 * Subclasses implement the behaviour for each specific command.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @param tasks The task list to act on.
     * @param ui The UI used to show messages to the user.
     * @param storage The storage used to persist changes.
     * @throws NickException If the command cannot be carried out.
     * @throws IOException If saving the tasks fails.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws NickException, IOException;

    /**
     * Returns whether this command should end the program.
     *
     * @return True if the program should exit after this command.
     */
    public boolean isExit() {
        return false;
    }
}
