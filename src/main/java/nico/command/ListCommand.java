package nico.command;

import nico.Storage;
import nico.TaskList;
import nico.Ui;

/**
 * Shows every task in the task list.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showTaskList(taskList);
    }
}