package wenwen.command;

import java.time.LocalDate;

import wenwen.storage.Storage;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Displays deadline and event tasks that occur on a specified date.
 */
public class OnDateCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a command that lists tasks occurring on a date.
     *
     * @param date date to query
     */
    public OnDateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOnDate(date, tasks.findOnDate(date));
    }
}
