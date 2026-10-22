package yao.command;

import java.time.LocalDate;
import java.util.ArrayList;
import yao.Storage;
import yao.TaskList;
import yao.Ui;
import yao.task.Task;

/**
 * Command to search for tasks occurring on a specified date.
 */
public class DateFilterCommand extends Command {
    private final LocalDate targetDate;

    /**
     * Constructs a DateFilterCommand with the specified target date.
     *
     * @param targetDate The date to search for matching tasks.
     */
    public DateFilterCommand(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ArrayList<Task> matchingTasks = tasks.findTasksOnDate(targetDate);
        ui.showTasksOnDate(matchingTasks, targetDate);
    }
}
