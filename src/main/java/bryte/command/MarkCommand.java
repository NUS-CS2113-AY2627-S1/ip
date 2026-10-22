package bryte.command;

import bryte.Storage;
import bryte.TaskList;
import bryte.Ui;
import bryte.exception.BryteException;

/**
 * Represents a command to mark or unmark a task.
 */
public class MarkCommand extends Command {
    private int index;
    private boolean isDone;

    /**
     * Constructs a MarkCommand.
     *
     * @param index  The index of the task to mark.
     * @param isDone True to mark as done, false to unmark.
     */
    public MarkCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }

    /**
     * Executes the mark or unmark command by updating the task's completion status and saving to storage.
     *
     * @param tasks The task list.
     * @param ui The user interface.
     * @param storage The storage for saving tasks.
     * @throws BryteException If the task index is invalid or an error occurs when saving.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BryteException {
        if (index < 0 || index >= tasks.size()) {
            throw new BryteException("Invalid task number.");
        }

        ui.showDivider();
        if (isDone) {
            ui.showMessage(" Nice! I've marked this task as done:");
        } else {
            ui.showMessage(" OK, I've marked this task as not done yet:");
        }
        tasks.get(index).setDone(isDone);
        storage.save(tasks.getTasks());
        ui.showMessage("   " + tasks.get(index).toString());
        ui.showDivider();
    }
}
