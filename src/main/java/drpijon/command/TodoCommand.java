package drpijon.command;

import drpijon.exception.DrPijonException;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.task.Todo;
import drpijon.ui.Ui;

/**
 * Adds a todo task.
 */
public class TodoCommand extends AddCommand {
    /**
     * Creates a todo command.
     *
     * @param description todo description
     */
    public TodoCommand(String description) {
        super(description);
    }

    /**
     * Creates, saves, and displays a todo task.
     *
     * @param tasks task list to update
     * @param ui user interface used for the response
     * @param storage storage used to persist the new task
     * @throws DrPijonException when the description is missing or saving fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DrPijonException {
        if (getDescription().isBlank()) {
            throw new DrPijonException("OI TODO DESCRIPTION CANT BE EMPTY >:( Try: todo read book");
        }

        addTask(new Todo(getDescription()), tasks, ui, storage);
    }
}
