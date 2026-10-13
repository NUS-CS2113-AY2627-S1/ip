package aglio.command;

import aglio.exception.AglioException;
import aglio.storage.Storage;
import aglio.task.TaskList;
import aglio.ui.Ui;

import java.io.IOException;

/** Represents an "unmark" command that marks a task as not done. */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Creates an UnmarkCommand for the given zero-based task index.
     *
     * @param index the zero-based index of the task to unmark
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AglioException {
        if (index < 0 || index >= tasks.getSize()) {
            throw new AglioException("Task number " + (index + 1)
                    + " does not exist. You have " + tasks.getSize() + " tasks.");
        }
        tasks.getTask(index).markAsNotDone();
        ui.showMarkNotDone(tasks.getTask(index));
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showSaveError(e.getMessage());
        }
    }
}
