package jamal.command;

import jamal.JamaLException;
import jamal.Storage;
import jamal.TaskList;
import jamal.Ui;
import jamal.task.Task;

/** Removes a task from the list. */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a command that deletes the task at the given index.
     *
     * @param index Zero-based index of the task to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JamaLException {
        Task removed = tasks.remove(index);
        storage.save(tasks.asList());
        ui.showMessage("gone:", "  " + removed, "you got " + tasks.countText() + " left.");
    }
}
