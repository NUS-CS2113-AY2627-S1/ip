package nico.command;

import nico.NicoException;
import nico.Storage;
import nico.TaskList;
import nico.Ui;
import nico.tasks.Task;

/**
 * Adds a todo, deadline or event to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that will add the given task.
     *
     * @param task The task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws NicoException {
        taskList.add(task);
        storage.save(taskList);
        ui.showMessage("Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskList.size() + " task(s) in the list.");
    }
}