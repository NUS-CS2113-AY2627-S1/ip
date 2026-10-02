package alfred.command;

import java.io.IOException;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;
import alfred.exception.AlfredException;

/**
 * Represents a command that can be executed by Alfred.
 */
public abstract class Command {

    /**
     * Executes this command using the application's task list, user interface, and storage.
     *
     * @param tasks Tasks managed by Alfred.
     * @param ui User interface used to display command results.
     * @param storage Storage used to persist task changes.
     * @throws AlfredException If the command cannot be applied to the current tasks.
     * @throws IOException If a task change cannot be saved.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws AlfredException, IOException;

    /**
     * Returns whether executing this command should end the application.
     *
     * @return True if this command exits Alfred.
     */
    public boolean isExit() {
        return false;
    }
}
