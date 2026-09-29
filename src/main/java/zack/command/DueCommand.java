package zack.command;

import zack.Ui;
import zack.task.Deadline;
import zack.task.Task;
import zack.task.TaskList;

import java.time.LocalDate;

/** Displays deadlines on a specified date using their original task numbers. */
public class DueCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a command to find deadlines on a specified date.
     *
     * @param date date to match against task deadlines
     */
    public DueCommand(LocalDate date) {
        this.date = date;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showMessage(" Deadlines on " + date + ":");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task instanceof Deadline
                    && ((Deadline) task).getDueDate().equals(date)) {
                ui.showMessage(" " + (i + 1) + "." + task);
                count++;
            }
        }
        ui.showMessage(" " + count + " matching deadlines.");
    }
}