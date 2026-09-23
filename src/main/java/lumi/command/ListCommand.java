package lumi.command;

import java.util.ArrayList;

import lumi.Parser;
import lumi.Storage;
import lumi.TaskList;
import lumi.Ui;
import lumi.task.Task;

/** Shows every task, numbered from 1, or says so when there is nothing to show. */
public class ListCommand extends Command {
    /**
     * Shows every task, or says the list is empty and how to start it.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.isEmpty()) {
            ui.show("Your list is empty. " + Parser.TODO_FORMAT);
            return;
        }
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Here are the tasks in your list:");
        int number = TaskList.FIRST_TASK_NUMBER;
        for (Task task : tasks.asList()) {
            lines.add(number + "." + task);
            number++;
        }
        ui.show(lines.toArray(new String[0]));
    }
}
