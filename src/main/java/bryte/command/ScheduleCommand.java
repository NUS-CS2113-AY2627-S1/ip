package bryte.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import bryte.Storage;
import bryte.TaskList;
import bryte.Ui;
import bryte.task.Task;

/**
 * Represents a command to list all tasks occurring on a specific date.
 */
public class ScheduleCommand extends Command {
    private LocalDate targetDate;

    /**
     * Constructs a ScheduleCommand for the given target date.
     *
     * @param targetDate The date to search for.
     */
    public ScheduleCommand(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    /**
     * Executes the schedule command by listing tasks occurring on the target date.
     *
     * @param tasks The task list.
     * @param ui The user interface.
     * @param storage The storage for saving tasks.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showDivider();
        ui.showMessage(" Here are the tasks happening on "
                + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

        int count = 1;
        for (Task task : tasks.getTasks()) {
            if (task.isOnDate(targetDate)) {
                ui.showMessage(" " + count + "." + task.toString());
                count++;
            }
        }

        if (count == 1) {
            ui.showMessage(" No tasks found on this date!");
        }
        ui.showDivider();
    }
}
