package kevie.commands;

import kevie.Kevie;
import kevie.Storage;
import kevie.UserInterface;
import kevie.exceptions.CmdSynNoArgException;
import kevie.exceptions.CmdSyntaxException;
import kevie.tasks.TaskList;
import kevie.tasks.Todo;

/**
 * Command that creates a simple todo task in the todo list.
 */
public class TodoCommand extends TaskSaveCommand {

    /**
     * Creates todo command.
     *
     * @param ui User interface to print messages in
     * @param taskList Todo list to modify
     * @param storage Storage to save modified todo list in.
     */
    public TodoCommand(UserInterface ui, TaskList taskList, Storage storage) {
        super("todo", ui, taskList, storage);
    }

    @Override
    public boolean execute(String arg) throws CmdSyntaxException {
        if (arg == null){
            throw new CmdSynNoArgException(this, ui);
        }
        Todo newTodo = new Todo(arg);
        taskList.addTask(newTodo);
        storage.save(taskList, ui);
        ui.botSpeak("Alright I added new todo to the list: ");
        ui.botSpeak(newTodo.toString(), true);
        ui.botSpeak("You now have " + taskList.getLength() + " tasks.", true);
        return false;
    }

    @Override
    public String syntax() {
        return "todo [Description]";
    }

    @Override
    public String example() {
        return "todo CS2113 iP bug fix";
    }
}
