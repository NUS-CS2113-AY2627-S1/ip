package mybff;

import java.io.IOException;

/** Deletes a task and restores its original position if saving fails. */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a deletion command for a parsed task index.
     * The index is checked against the current list when the command executes.
     *
     * @param index the zero-based index of the task to remove
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /** Deletes and saves a valid task, restoring its original position if saving fails. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (!tasks.isValidIndex(index)) {
            ui.printInvalidTaskNumber();
            return;
        }
        Task removedTask = tasks.delete(index);
        try {
            storage.save(tasks);
        } catch (IOException | SecurityException exception) {
            tasks.insert(index, removedTask);
            ui.printSaveError();
            return;
        }
        ui.printTaskDeleted(removedTask, tasks.size());
    }
}
