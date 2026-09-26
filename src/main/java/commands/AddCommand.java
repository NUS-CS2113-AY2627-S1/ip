package commands;

import storage.Storage;
import tasks.Task;
import tasks.TaskList;
import ui.Ui;

/**
 * Adds a parsed task, displays its confirmation, and saves the updated list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command for any supported task type.
     *
     * @param task Task whose description and format have been validated by the parser.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        tasks.add(task);
        ui.showAddedTask(task, tasks.size());
        Storage.save(tasks.getTasks());
    }
}
