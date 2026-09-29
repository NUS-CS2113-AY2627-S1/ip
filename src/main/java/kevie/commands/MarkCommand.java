package kevie.commands;

import kevie.Kevie;
import kevie.Storage;
import kevie.UserInterface;
import kevie.exceptions.*;
import kevie.tasks.Task;
import kevie.tasks.TaskList;

/**
 * Command that marks a task as done.
 */
public class MarkCommand extends TaskSaveCommand {

    /**
     * Creates mark command.
     *
     * @param ui User interface to print messages in
     * @param taskList Todo list to modify
     * @param storage Storage to save modified todo list in.
     */
    public MarkCommand(UserInterface ui, TaskList taskList, Storage storage) {
        super("mark", ui, taskList, storage);
    }

    @Override
    public boolean execute(String arg) throws CmdSyntaxException, TaskNoException {
        if (arg == null){
            throw new CmdSynNoArgException(this, ui);
        }

        int markNo = -1;

        try {
            markNo = Integer.parseInt(arg);
        } catch (Exception e) {
            throw new TaskNoParseException(ui);
        }

        if (markNo > taskList.getLength()) {
            throw new TaskNoExceedException(markNo, taskList.getLength(), ui);
        } else if (markNo < 1) {
            throw new TaskNoNonPosException(ui);
        }

        Task markedTask = taskList.getTask(markNo - 1);
        if (markedTask.isDone()){
            ui.botSpeak("Task " + (markNo) + " is marked as done already:");
            ui.botSpeak(markedTask.toString(), true);
            return false;
        }
        markedTask.setDone(true);
        storage.save(taskList, ui);
        ui.botSpeak("Ok! I have marked a task as done: ");
        ui.botSpeak(markedTask.toString(), true);
        return false;
    }

    @Override
    public void help() {
        super.help();
        ui.botSpeak("To see which number correspond to your task, do \"list\".", true);
    }

    @Override
    public String syntax() {
        return "mark [Task No.]";
    }

    @Override
    public String example() {
        return "mark 3";
    }
}
