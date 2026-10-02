package foodielover.command;

import java.time.LocalDate;
import java.util.List;

import foodielover.storage.Storage;
import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Represents a command to display tasks occurring on a specific date.
 */
public class DateFilterCommand extends Command {
    /** Target date to filter tasks by. */
    private final LocalDate targetDate;

    /**
     * Constructs a DateFilterCommand with the specified target date.
     *
     * @param targetDate Date to filter tasks by.
     */
    public DateFilterCommand(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    /**
     * Executes the date filter command by finding and displaying tasks on the target date.
     *
     * @param tasks Current task list.
     * @param ui User interface for user interactions.
     * @param storage Storage handler for persisting changes.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.getTasksOnDate(targetDate);
        ui.showTasksOnDate(matchingTasks, targetDate);
    }
}
