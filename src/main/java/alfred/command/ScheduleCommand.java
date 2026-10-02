package alfred.command;

import java.time.LocalDate;

import alfred.Storage;
import alfred.TaskList;
import alfred.Ui;

/**
 * Displays deadlines and events that occur on a specified date.
 */
public class ScheduleCommand extends Command {

    private final LocalDate date;

    /**
     * Creates a command that displays scheduled tasks on the specified date.
     *
     * @param date Date whose scheduled tasks should be displayed.
     */
    public ScheduleCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showSchedule(tasks.getTasksOn(date), date);
    }
}
