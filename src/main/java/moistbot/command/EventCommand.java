package moistbot.command;

import moistbot.task.Task;
import moistbot.task.TaskManager;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Adds an event task with a start and end time to the task list.
 */
public final class EventCommand extends AddCommand {
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final LocalDate endDate;
    private final LocalTime endTime;

    /**
     * Creates an event command for the supplied description and times.
     *
     * @param description The event task description
     * @param startDate The event start date
     * @param startTime The optional event start time
     * @param endDate The event end date
     * @param endTime The optional event end time
     */
    public EventCommand(String description, LocalDate startDate, LocalTime startTime,
            LocalDate endDate, LocalTime endTime) {
        super(description);
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    /**
     * Adds the event task represented by this command.
     *
     * @param tasks The task list to modify
     * @return The event task that was added
     */
    @Override
    protected Task createTask(TaskManager tasks) {
        return tasks.addEvent(getDescription(), startDate, startTime, endDate, endTime);
    }
}
