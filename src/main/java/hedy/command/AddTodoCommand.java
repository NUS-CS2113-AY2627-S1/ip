package hedy.command;

import hedy.exception.HedyException;
import hedy.storage.Storage;
import hedy.task.Task;
import hedy.task.TaskList;
import hedy.task.Todo;
import hedy.ui.Ui;

/** Adds a todo to the task list and saves it. */
public class AddTodoCommand extends Command {
    private final String description;

    /**
     * Creates a command that adds a todo.
     *
     * @param description what needs to be done
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HedyException {
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAdded(task, tasks.size());
    }
}
