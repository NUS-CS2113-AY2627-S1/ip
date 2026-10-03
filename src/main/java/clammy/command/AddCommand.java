package clammy.command;

import clammy.storage.Storage;
import clammy.task.Task;
import clammy.task.TaskList;
import clammy.ui.Ui;

/**
 * Adds a todo, deadline, or event to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the supplied task.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task, displays confirmation, and saves the updated list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        saveTasks(tasks, ui, storage);
    }
}
