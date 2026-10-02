package nico.command;

import nico.NicoException;
import nico.Storage;
import nico.TaskList;
import nico.Ui;
import nico.tasks.Task;

/**
 * Marks a task as done, or as not done.
 */
public class MarkCommand extends Command {
    private final int index;
    private final boolean isDone;

    /**
     * Creates a command that will mark the task at the given position.
     *
     * @param index 0-based position of the task.
     * @param isDone True to mark the task as done, false to mark it as not done.
     */
    public MarkCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NicoException {
        Task task = taskList.setDone(index, isDone);
        storage.save(taskList);
        String header = isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        ui.showMessage(header, "  " + task);
    }
}