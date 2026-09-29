package kevie.commands;

import kevie.Kevie;
import kevie.Storage;
import kevie.UserInterface;
import kevie.exceptions.*;
import kevie.tasks.Task;
import kevie.tasks.TaskList;

/**
 * Command that deletes a task in the todo list.
 */
public class DeleteCommand extends TaskSaveCommand {

    /**
     * Creates delete command.
     *
     * @param ui User interface to print messages in
     * @param taskList Todo list to modify
     * @param storage Storage to save modified todo list in.
     */
    public DeleteCommand(UserInterface ui, TaskList taskList, Storage storage) {
        super("delete", ui, taskList, storage);
    }

    @Override
    public boolean execute(String arg) throws CmdSyntaxException, TaskNoException {
        if (arg == null){
            throw new CmdSynNoArgException(this, ui);
        }

        int deleteNo = -1;

        try {
            deleteNo = Integer.parseInt(arg);
        } catch (Exception e) {
            throw new TaskNoParseException(ui);
        }

        if (deleteNo > taskList.getLength()) {
            throw new TaskNoExceedException(deleteNo, taskList.getLength(), ui);
        } else if (deleteNo < 1) {
            throw new TaskNoNonPosException(ui);
        }

        Task deleteTask = taskList.getTask(deleteNo - 1);
        taskList.deleteTask(deleteNo - 1);
        storage.save(taskList, ui);
        ui.botSpeak("Can! I have deleted the following task: ");
        ui.botSpeak(deleteTask.toString(), true);
        ui.botSpeak("You now have " + taskList.getLength() + " tasks.", true);
        return false;
    }

    @Override
    public void help() {
        super.help();
        ui.botSpeak("To see which number correspond to your task, do \"list\".", true);
    }

    @Override
    public String syntax() {
        return "delete [Task No.]";
    }

    @Override
    public String example() {
        return "delete 3";
    }
}
