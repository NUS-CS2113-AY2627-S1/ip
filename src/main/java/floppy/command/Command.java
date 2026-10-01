package floppy.command;

import floppy.FloppyException;
import floppy.Parser;
import floppy.Storage;
import floppy.TaskList;
import floppy.Ui;
import floppy.task.Task;

/**
 * One command from the user, ready to be carried out.
 *
 * <p>Each kind of command is a subclass that knows how to do its own work, so adding a
 * command means adding a class rather than extending a chain of if-else branches.
 */
public abstract class Command {

    /** Constructs a command. Called implicitly by each subclass. */
    protected Command() {
    }

    /**
     * Carries out this command.
     *
     * @param tasks   the tasks Floppy is holding.
     * @param ui      where messages are shown.
     * @param storage where the tasks are saved.
     * @throws FloppyException if the command cannot be carried out as typed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws FloppyException;

    /**
     * Returns whether Floppy should stop after this command. Only the exit command
     * overrides this.
     *
     * @return true if this command ends the conversation.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Returns the task named by the number in the user's command. Shared by the
     * commands that act on an existing task.
     *
     * @param tasks the tasks Floppy is holding.
     * @param input the whole line the user entered, already stripped.
     * @return the task the user picked.
     * @throws FloppyException if the number is missing, not a number, or names no stored task.
     */
    protected Task findTask(TaskList tasks, String input) throws FloppyException {
        int taskNumber = Parser.parseTaskNumber(input);

        if (tasks.isEmpty()) {
            throw new FloppyException("There's nothing on me to "
                    + Parser.commandWordOf(input).toLowerCase()
                    + " yet. Add a task first, e.g. 'todo borrow book'.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new FloppyException("I have " + Ui.describeCount(tasks.size())
                    + ". There is nothing at number " + Parser.argumentOf(input) + ".");
        }

        return tasks.get(taskNumber - 1);
    }
}
