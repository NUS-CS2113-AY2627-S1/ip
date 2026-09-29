package zack.command;

import zack.Ui;
import zack.ZackException;
import zack.task.TaskList;

/**
 * Represents a user command that operates on tasks or displays information.
 */
public abstract class Command {
    /**
     * Executes this command and displays its result.
     *
     * @param tasks task list used by the command
     * @param ui user interface used to display messages
     * @throws ZackException if the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui) throws ZackException;

    /**
     * Indicates whether the application should exit after this command.
     *
     * @return true for an exit command; false otherwise
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Indicates whether successful execution requires saving the task list.
     *
     * @return true if this command modifies tasks; false otherwise
     */
    public boolean changesTasks() {
        return false;
    }
}