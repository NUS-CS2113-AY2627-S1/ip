package turing.command;

import turing.Storage;
import turing.TuringException;
import turing.Ui;
import turing.task.TaskList;

/**
 * One instruction the user has given, ready to be carried out.
 * Every command the chatbot understands is a subclass, and each one knows
 * both what it changes and what it tells the user afterwards. Turing can
 * therefore run any command without knowing which one it is, and adding a
 * command means adding a class rather than another branch to an existing
 * method.
 */
public abstract class Command {
    /**
     * Carries this command out.
     *
     * @param tasks Task list the command works on.
     * @param ui Voice the command reports its outcome through.
     * @param storage Save file the command writes to if it changes the list.
     * @throws TuringException If the command cannot be carried out as typed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws TuringException;

    /**
     * Returns whether the conversation ends after this command. Only the exit
     * command overrides this, so every other command carries on as usual.
     *
     * @return True if the chatbot should stop reading input.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Writes the task list out, explaining a failure to the user. The change
     * the user just made stays in the list either way, so a save that fails is
     * worth reporting but not worth undoing the command over.
     *
     * @param tasks Task list to write.
     * @param ui Voice any complaint is made through.
     * @param storage Save file to write to.
     */
    protected static void save(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks);
        } catch (TuringException exception) {
            ui.showError(exception);
        }
    }

    /**
     * Checks that a task carrying the given number is stored, which the
     * commands naming a task have to do before they can act on it.
     *
     * @param tasks Task list to look in.
     * @param taskNumber Task number as shown to the user, starting at 1.
     * @param commandWord Command word the user typed, quoted back in any error message.
     * @throws TuringException If no stored task carries that number.
     */
    protected static void requireStoredTaskNumber(TaskList tasks, int taskNumber, String commandWord)
            throws TuringException {
        if (tasks.isEmpty()) {
            throw new TuringException("Your list is empty, so there is nothing to " + commandWord + " yet.",
                    "Add a task first, e.g. todo borrow book");
        }

        if (!tasks.hasTaskNumber(taskNumber)) {
            throw new TuringException("There is no task " + taskNumber + " in your list.",
                    "Please pick a number from 1 to " + tasks.getTaskCount() + ", or type list to see them.");
        }
    }
}
