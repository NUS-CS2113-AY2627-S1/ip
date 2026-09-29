package kevie.commands;

import kevie.Kevie;
import kevie.UserInterface;
import kevie.exceptions.CmdSynNoArgException;
import kevie.exceptions.CmdSyntaxException;
import kevie.tasks.TaskList;

import java.util.ArrayList;

/**
 * Command that find all tasks with a specified keyword in the todo list.
 */
public class FindCommand extends TaskCommand{

    /**
     * Creates find command.
     *
     * @param ui User interface to print messages in
     * @param taskList Todo list to access
     */
    public FindCommand(UserInterface ui, TaskList taskList) {
        super("find", ui, taskList);
    }

    @Override
    public boolean execute(String arg) throws CmdSyntaxException {
        if (arg == null){
            throw new CmdSynNoArgException(this, ui);
        }

        ArrayList<Integer> taskIndexes = taskList.keywordFindTasks(arg.trim());

        if (taskIndexes.size() <= 0){
            ui.botSpeak("There are no task with keyword \"" + arg.trim() + "\" in your list.");
            return false;
        }

        ui.botSpeak("Here are the tasks with the keyword \"" + arg.trim() + "\" in your list: ");
        for (int i : taskIndexes){
            ui.botSpeak((i + 1) + ". " + taskList.getTask(i).toString(), true);
        }
        return false;
    }

    @Override
    public String syntax() {
        return "find [Keyword]";
    }

    @Override
    public String example() {
        return "find CS2113";
    }
}
