package aglio.command;

import aglio.storage.Storage;
import aglio.task.Task;
import aglio.task.TaskList;
import aglio.ui.Ui;

import java.io.IOException;

/**
 * Represents a command that adds a task (todo, deadline, or event)
 * to the task list. The specific Task object is created by the Parser
 * and passed to this command.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates an AddCommand that will add the given task.
     *
     * @param task the task to add (a Todo, Deadline, or Event)
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.addTask(task);
        ui.showTaskAdded(task, tasks.getSize());
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showSaveError(e.getMessage());
        }
    }
}
