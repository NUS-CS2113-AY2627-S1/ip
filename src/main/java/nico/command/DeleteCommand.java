package nico.command;

import nico.NicoException;
import nico.Storage;
import nico.TaskList;
import nico.Ui;
import nico.tasks.Task;

/**
 * Removes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a command that will delete the task at the given position.
     *
     * @param index 0-based position of the task to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NicoException {
        Task removedTask = taskList.delete(index);
        storage.save(taskList);
        ui.showMessage("Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + taskList.size() + " task(s) in the list.");
    }
}