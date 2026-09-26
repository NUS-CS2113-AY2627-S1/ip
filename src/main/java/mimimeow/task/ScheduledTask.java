package mimimeow.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Defines date-based operations supported by scheduled tasks. */
public interface ScheduledTask {
    /**
     * Returns whether this task occurs on the specified date.
     *
     * @param date date to check
     * @return true if the task occurs on the date
     */
    boolean occursOn(LocalDate date);

    /**
     * Returns whether this task is upcoming at the specified date and time.
     *
     * @param dateTime date and time to compare against
     * @return true if the task is upcoming
     */
    boolean isUpcomingAt(LocalDateTime dateTime);

    /**
     * Returns whether this task is overdue at the specified date and time.
     *
     * @param dateTime date and time to compare against
     * @return true if the task is overdue
     */
    boolean isOverdueAt(LocalDateTime dateTime);

    /**
     * Returns the date and time used to order this task chronologically.
     *
     * @return task schedule time
     */
    TaskDateTime getScheduleTime();
}
