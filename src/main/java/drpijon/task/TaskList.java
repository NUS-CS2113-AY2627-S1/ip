package drpijon.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Stores the tasks managed by the Dr. Pijon application.
 */
public class TaskList implements Iterable<Task> {
    /** Tasks maintained in their display and storage order. */
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
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
     * Returns the task at a zero-based position.
     *
     * @param index zero-based task position
     * @return task at the requested position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based position.
     *
     * @param index zero-based task position
     * @return removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list contains no tasks.
     *
     * @return true when the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns typed tasks occurring on the specified date.
     *
     * @param date date to search for
     * @return matching tasks in their original order
     */
    public List<Task> findOnDate(LocalDate date) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns tasks whose descriptions contain a keyword, ignoring letter case.
     *
     * @param keyword keyword to search for
     * @return matching tasks in their original order
     */
    public List<Task> find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns an iterator over tasks in their current order.
     *
     * @return task iterator
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
