package aglio.command;

import aglio.exception.AglioException;
import aglio.storage.Storage;
import aglio.task.Task;
import aglio.task.TaskList;
import aglio.ui.Ui;

import java.io.IOException;

/** Represents a "delete" command that removes a task from the list. */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a DeleteCommand for the given zero-based task index.
     *
     * @param index the zero-based index of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AglioException {
        if (index < 0 || index >= tasks.getSize()) {
            throw new AglioException("Task number " + (index + 1)
                    + " does not exist. You have " + tasks.getSize() + " tasks.");
        }
        Task removedTask = tasks.deleteTask(index);
        ui.showDelete(removedTask, tasks.getSize());
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showSaveError(e.getMessage());
        }
    }
}
