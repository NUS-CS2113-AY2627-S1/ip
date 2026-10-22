package isa.command;

import isa.storage.Storage;
import isa.task.Deadline;
import isa.task.Event;
import isa.task.Task;
import isa.task.TaskList;
import isa.ui.Ui;

import java.time.LocalDate;

/**
 * Lists deadlines due and events occurring on a selected date.
 */
public class OnDateCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a command for a selected date.
     *
     * @param date Date whose tasks should be shown.
     */
    public OnDateCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * Displays deadlines and events that occur on the selected date.
     *
     * @param tasks Current task list.
     * @param ui Console interface for matching tasks.
     * @param storage Task storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showOnDateHeader(date);
        boolean hasMatches = false;

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            boolean isDeadlineOnDate = task instanceof Deadline
                    && ((Deadline) task).getDueDate().equals(date);
            boolean isEventOnDate = task instanceof Event
                    && ((Event) task).occursOn(date);

            if (isDeadlineOnDate || isEventOnDate) {
                ui.showTaskOnDate(i + 1, task);
                hasMatches = true;
            }
        }

        if (!hasMatches) {
            ui.showNoTasksOnDate();
        }
    }
}
