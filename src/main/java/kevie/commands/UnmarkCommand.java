package kevie.commands;

import kevie.Kevie;
import kevie.Storage;
import kevie.UserInterface;
import kevie.exceptions.*;
import kevie.tasks.Task;
import kevie.tasks.TaskList;

/**
 * Command that set a task in the todo list as not done.
 */
public class UnmarkCommand extends TaskSaveCommand {

    /**
     * Creates unmark command.
     *
     * @param ui User interface to print messages in
     * @param taskList Todo list to modify
     * @param storage Storage to save modified todo list in.
     */
    public UnmarkCommand(UserInterface ui, TaskList taskList, Storage storage) {
        super("unmark", ui, taskList, storage);
    }

    @Override
    public boolean execute(String arg) throws CmdSyntaxException, TaskNoException {
        if (arg == null){
            throw new CmdSynNoArgException(this, ui);
        }

        int unmarkNo = -1;

        try {
            unmarkNo = Integer.parseInt(arg);
        } catch(NumberFormatException e) {
            throw new TaskNoParseException(ui);
        }

        if (unmarkNo > taskList.getLength()) {
            throw new TaskNoExceedException(unmarkNo, taskList.getLength(), ui);
        } else if (unmarkNo < 1) {
            throw new TaskNoNonPosException(ui);
        }

        Task unmarkedTask = taskList.getTask(unmarkNo - 1);
        if (!unmarkedTask.isDone()){
            ui.botSpeak("Task " + (unmarkNo) + " is marked as not done already:");
            ui.botSpeak(unmarkedTask.toString(), true);
            return false;
        }
        unmarkedTask.setDone(false);
        storage.save(taskList, ui);
        ui.botSpeak("Ok! I have marked a task as undone:");
        ui.botSpeak(unmarkedTask.toString(), true);
        return false;
    }

    @Override
    public void help() {
        super.help();
        ui.botSpeak("To see which number correspond to your task, do \"list\".", true);
    }

    @Override
    public String syntax() {
        return "unmark [Task No.]";
    }

    @Override
    public String example() {
        return "unmark 1";
    }
}
