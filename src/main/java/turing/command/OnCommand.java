package turing.command;

import java.time.LocalDate;
import java.util.List;

import turing.Storage;
import turing.Ui;
import turing.task.Task;
import turing.task.TaskList;

/**
 * Shows what one day holds: the deadlines due that day and the events running
 * through it. A task list answers "what do I have to do" well enough on its
 * own, but not "what does Thursday look like", which is the question worth
 * asking once the list carries real dates.
 */
public class OnCommand extends Command {
    /** Day the user is asking about. */
    private final LocalDate date;

    /**
     * Creates a command that shows what the given day holds.
     *
     * @param date Day to look at.
     */
    public OnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> tasksOnDate = tasks.getTasksOn(date);
        ui.showTasksOn(date, tasksOnDate);
    }
}
