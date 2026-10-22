package jamal.command;

import jamal.JamaLException;
import jamal.Storage;
import jamal.TaskList;
import jamal.Ui;
import jamal.task.Task;

/** Marks a task as done, or unmarks it back to not done. */
public class MarkCommand extends Command {
    private final int index;
    private final boolean isDone;

    /**
     * Creates a command that sets the done status of a task.
     *
     * @param index Zero-based index of the task.
     * @param isDone True to mark the task, false to unmark it.
     */
    public MarkCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JamaLException {
        Task task = tasks.get(index);
        task.setDone(isDone);
        storage.save(tasks.asList());
        String status = isDone ? "marked" : "unmarked";
        ui.showMessage("task " + (index + 1) + " is " + status, "  " + task);
    }
}
