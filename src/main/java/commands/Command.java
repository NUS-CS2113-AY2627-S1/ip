package commands;

import exception.SamSquareException;
import tasks.TaskList;
import ui.Ui;

/**
 * Defines an executable user action and whether it ends the console session.
 */
public abstract class Command {
    /**
     * Performs this command using the current tasks and console interface.
     *
     * @param tasks Current task list.
     * @param ui Console interface for command responses.
     * @throws SamSquareException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui) throws SamSquareException;

    /**
     * Indicates whether the application should stop after executing this command.
     *
     * @return False unless the command explicitly requests an exit.
     */
    public boolean isExit() {
        return false;
    }
}
