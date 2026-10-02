package chre.command;

import chre.data.TaskList;
import chre.ui.Ui;
import chre.storage.Storage;
import chre.exception.ChreException;

/**
 * Adds a todo task to the task list.
 */
public class TodoCommand extends Command {
    private String name;

    /**
     * Creates a TodoCommand with the given task name.
     *
     * @param name the name of the todo task
     */
    public TodoCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws ChreException {
        taskList.addTodo(name);
        ui.showTaskAdded(taskList.getLastTask(), taskList.size());
        storage.save(taskList.getTasks());
    }
}
