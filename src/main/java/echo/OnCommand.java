package echo;

import java.time.LocalDate;

/**
 * Shows the deadlines due on, and the events spanning, a given date.
 */
public class OnCommand extends Command {
    /** The date whose deadlines and events to show. */
    private final LocalDate date;

    /**
     * Creates a command that lists the tasks falling on a date.
     *
     * @param date the date to check.
     */
    public OnCommand(LocalDate date) {
        this.date = date;
    }

    /** Shows the deadlines due on, and the events spanning, the date. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOn(date, tasks.findOn(date));
    }
}
