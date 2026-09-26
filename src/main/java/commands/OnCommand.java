package commands;

import java.time.LocalDate;

import tasks.TaskList;
import ui.Ui;

/**
 * Displays deadlines due on a calendar date without changing or saving tasks.
 */
public class OnCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a deadline search for a validated date.
     *
     * @param date Calendar date to match against deadline due dates.
     */
    public OnCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showDeadlinesOn(tasks.findDeadlinesOn(date), date);
    }
}
