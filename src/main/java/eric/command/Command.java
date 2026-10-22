package eric.command;

import java.io.IOException;

import eric.exception.EricException;
import eric.storage.Storage;
import eric.task.TaskList;
import eric.ui.Ui;

/**
 * A command that the user can give to Eric. Each kind of command is a subclass that knows how to
 * carry itself out. The steps that several commands share are provided here.
 */
public abstract class Command {
    /**
     * Carries out this command.
     *
     * @param tasks The task list, which the command may change.
     * @param ui Used to tell the user what happened.
     * @param storage Used to save the task list after a change.
     * @throws EricException If the command cannot be carried out.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws EricException;

    /** Returns true if carrying out this command ends the program. */
    public boolean isExit() {
        return false;
    }

    /**
     * Converts the 1-based task number typed by the user into an index of the task list.
     *
     * @param tasks The task list.
     * @param taskNumber Number that the user typed, as shown by the list command.
     * @param commandWord Word of the command, used in the error message.
     * @return Zero-based index of the task.
     * @throws EricException If the list is empty or has no task with that number.
     */
    protected static int findTaskIndex(TaskList tasks, int taskNumber, String commandWord)
            throws EricException {
        if (tasks.isEmpty()) {
            throw new EricException("There are no tasks to " + commandWord + " yet.",
                    "Add a task first, e.g. " + AddCommand.EXAMPLE_TODO);
        }
        int index = taskNumber - 1;
        if (!tasks.isValidIndex(index)) {
            throw new EricException("Task " + taskNumber + " doesn't exist.",
                    "Choose a number from 1 to " + tasks.size() + ". Type list to see the tasks.");
        }
        return index;
    }

    /**
     * Saves the task list, and tells the user if that fails. A failure to save does not make the
     * command fail, because the command has already changed the list.
     */
    protected static void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException exception) {
            ui.showError("I couldn't save your tasks to " + storage.getFilePath() + ".",
                    "Check that the folder can be written to. Reason: " + exception.getMessage());
        }
    }
}
