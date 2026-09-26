package mimimeow.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Stores and manages the tasks created in MimiMeow.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /** Creates an empty task list. */
    public TaskList() {
    }

    /**
     * Adds a task to the dynamic list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Adds a task at the specified zero-based index.
     *
     * @param index position at which to add the task
     * @param task task to add
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Deletes the specified task from the list if it is present.
     *
     * @param task task to delete
     */
    public void delete(Task task) {
        tasks.remove(task);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return number of stored tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index position of the task
     * @return task at the specified position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Deletes and returns the task at the specified zero-based index.
     *
     * @param index position of the task to delete
     * @return deleted task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }


    /**
     * Returns scheduled tasks that occur on the specified date.
     *
     * @param date date to query
     * @return matching tasks in chronological order
     */
    /** Returns tasks whose descriptions contain the specified keyword. */
    public List<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.hasDescriptionContaining(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /** Returns scheduled tasks that occur on the specified date. */
    public List<Task> findTasksOccurringOn(LocalDate date) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof ScheduledTask scheduledTask && scheduledTask.occursOn(date)) {
                matchingTasks.add(task);
            }
        }
        matchingTasks.sort(createScheduleComparator());
        return matchingTasks;
    }

    /**
     * Returns incomplete scheduled tasks that are upcoming at the specified time.
     *
     * @param dateTime date and time to compare against
     * @return upcoming tasks in chronological order
     */
    public List<Task> findUpcomingTasks(LocalDateTime dateTime) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isDone() && task instanceof ScheduledTask scheduledTask
                    && scheduledTask.isUpcomingAt(dateTime)) {
                matchingTasks.add(task);
            }
        }
        matchingTasks.sort(createScheduleComparator());
        return matchingTasks;
    }

    /**
     * Returns incomplete scheduled tasks that are overdue at the specified time.
     *
     * @param dateTime date and time to compare against
     * @return overdue tasks in chronological order
     */
    public List<Task> findOverdueTasks(LocalDateTime dateTime) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isDone() && task instanceof ScheduledTask scheduledTask
                    && scheduledTask.isOverdueAt(dateTime)) {
                matchingTasks.add(task);
            }
        }
        matchingTasks.sort(createScheduleComparator());
        return matchingTasks;
    }

    /** Creates a comparator that orders scheduled tasks chronologically. */
    private Comparator<Task> createScheduleComparator() {
        return Comparator.comparing(task -> ((ScheduledTask) task).getScheduleTime());
    }
}
