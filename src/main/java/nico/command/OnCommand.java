package nico.command;

import nico.Storage;
import nico.TaskList;
import nico.Ui;

import java.time.LocalDate;

/**
 * Shows the deadlines and events that fall on a particular date.
 */
public class OnCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a command that will show tasks on the given date.
     *
     * @param date The date to look up.
     */
    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) {
        ui.showTasksOn(date, taskList.getTasksOn(date));
    }
}