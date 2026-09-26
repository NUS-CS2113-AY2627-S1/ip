package moistbot.command;

import moistbot.task.Task;
import moistbot.task.TaskManager;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Adds a task with a deadline to the task list.
 */
public final class DeadlineCommand extends AddCommand {
    private final LocalDate deadlineDate;
    private final LocalTime deadlineTime;

    /**
     * Creates a deadline command for the supplied description and deadline.
     *
     * @param description The deadline task description
     * @param deadlineDate The deadline date
     * @param deadlineTime The optional deadline time
     */
    public DeadlineCommand(String description, LocalDate deadlineDate, LocalTime deadlineTime) {
        super(description);
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

    /**
     * Adds the deadline task represented by this command.
     *
     * @param tasks The task list to modify
     * @return The deadline task that was added
     */
    @Override
    protected Task createTask(TaskManager tasks) {
        return tasks.addDeadline(getDescription(), deadlineDate, deadlineTime);
    }
}
