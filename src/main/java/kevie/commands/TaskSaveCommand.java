package kevie.commands;

import kevie.Storage;
import kevie.UserInterface;
import kevie.tasks.TaskList;

/**
 * Command that would modify and saves the todo list.
 */
public abstract class TaskSaveCommand extends TaskCommand{

    protected Storage storage;

    /**
     * Creates command that modifies and saves the todo list.
     *
     * @param id Command keyword
     * @param ui User interface to print messages in
     * @param taskList Todo list to modify
     * @param storage Storage to save modified todo list in.
     */
    public TaskSaveCommand(String id, UserInterface ui, TaskList taskList, Storage storage) {
        super(id, ui, taskList);
        this.storage = storage;
    }
}
