package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.Task;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Provides shared behavior for commands that add a task.
 */
public abstract class AddCommand extends Command {
    /** User-provided description and type-specific details for the task. */
    private final String description;

    /**
     * Creates an add command with the user-provided task description.
     *
     * @param description task description and type-specific details
     */
    protected AddCommand(String description) {
        this.description = description;
    }

    /**
     * Returns the task description supplied to this command.
     *
     * @return task description and type-specific details
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Adds a task, saves the updated list, and displays the result.
     *
     * @param task task to add
     * @param tasks task list to update
     * @param ui user interface used for the response
     * @param storage storage used to persist the update
     * @throws DrPijonException when the task list cannot be saved
     */
    protected void addTask(Task task, TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
