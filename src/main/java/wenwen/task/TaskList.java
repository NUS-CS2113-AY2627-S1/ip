package wenwen.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import wenwen.exception.WenwenException;

/**
 * Owns Wenwen's task collection and provides task-related operations.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks initial tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns a task using a one-based task number.
     *
     * @param taskNumber one-based task number
     * @return the requested task
     * @throws WenwenException if the task number is outside the list
     */
    public Task getByNumber(int taskNumber) throws WenwenException {
        return tasks.get(toIndex(taskNumber));
    }

    /**
     * Removes and returns a task using a one-based task number.
     *
     * @param taskNumber one-based task number
     * @return the removed task
     * @throws WenwenException if the task number is outside the list
     */
    public Task delete(int taskNumber) throws WenwenException {
        return tasks.remove(toIndex(taskNumber));
    }

    /**
     * Returns a task using its zero-based position for display iteration.
     *
     * @param index zero-based position
     * @return task at the position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view for persistence.
     *
     * @return unmodifiable task view
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns tasks whose deadline or event period falls on a date.
     *
     * @param date date to search
     * @return matching tasks in their original order
     */
    public TaskList findOnDate(LocalDate date) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matchingTasks.add(task);
            }
        }
        return new TaskList(matchingTasks);
    }

    /**
     * Returns tasks whose descriptions contain a keyword, ignoring case.
     *
     * @param keyword text to search for
     * @return matching tasks in their original order
     */
    public TaskList find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return new TaskList(matchingTasks);
    }

    /**
     * Converts and validates a one-based task number as a list index.
     *
     * @param taskNumber one-based task number
     * @return zero-based list index
     * @throws WenwenException if the task number is outside the list
     */
    private int toIndex(int taskNumber) throws WenwenException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new WenwenException("Task number " + taskNumber + " is not in your list.");
        }
        return taskNumber - 1;
    }
}
