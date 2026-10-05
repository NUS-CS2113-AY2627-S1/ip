package hedy.command;

import java.time.LocalDate;

import hedy.storage.Storage;
import hedy.task.TaskList;
import hedy.ui.Ui;

/** Lists the deadlines that are due on one date. */
public class OnCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a command that shows deadlines due on a date.
     *
     * @param date the day to look up
     */
    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showDeadlinesOn(date, tasks.deadlinesOn(date));
    }
}
