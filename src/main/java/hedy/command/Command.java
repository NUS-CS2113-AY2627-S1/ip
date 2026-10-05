package hedy.command;

import hedy.exception.HedyException;
import hedy.storage.Storage;
import hedy.task.TaskList;
import hedy.ui.Ui;

/**
 * One action Hedy can take after reading a user command.
 * Subclasses such as {@link DeleteCommand} and {@link ExitCommand} do the actual work.
 */
public abstract class Command {

    /**
     * Carries out this command.
     *
     * @param tasks the current task list
     * @param ui where replies are printed
     * @param storage where the list is saved
     * @throws HedyException if the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws HedyException;

    /**
     * Returns true when Hedy should exit after this command.
     * Most commands keep the program running.
     */
    public boolean isExit() {
        return false;
    }
}
