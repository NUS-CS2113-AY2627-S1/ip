package kevie.commands;

import kevie.UserInterface;
import kevie.exceptions.KevieException;
import kevie.tasks.TaskList;

/**
 * Command that would modify the todo list.
 */
public abstract class TaskCommand extends Command{

    protected TaskList taskList;

    /**
     * Creates command that modifies the todo list.
     *
     * @param id Command keyword
     * @param ui User interface to print messages in
     * @param taskList Todo list to modify
     */
    public TaskCommand(String id, UserInterface ui, TaskList taskList) {
        super(id, ui);
        this.taskList = taskList;
    }
}
