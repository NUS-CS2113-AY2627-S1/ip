package echo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the in-memory list of tasks and the operations to add, remove and
 * look up tasks in it.
 */
public class TaskList {
    /** The tasks, in the order they were added. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Creates a task list pre-populated with the given tasks, e.g. ones just
     * loaded from disk.
     *
     * @param tasks the initial tasks, in order.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given 0-based index.
     *
     * @param index position of the task to remove.
     * @return the removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given 0-based index.
     *
     * @param index position of the task.
     * @return the task at that position.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns how many tasks are in the list.
     *
     * @return the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks whose description contains the given keyword, in list order.
     *
     * @param keyword the text to look for; matching is case-sensitive.
     * @return the matching tasks, possibly empty.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.containsKeyword(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the tasks that fall on the given date (deadlines due that day,
     * events spanning it), in list order.
     *
     * @param date the date to check.
     * @return the matching tasks, possibly empty.
     */
    public List<Task> findOn(LocalDate date) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isOn(date)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the tasks as a plain list, in order, for display or saving.
     *
     * @return an unmodifiable copy of the tasks.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
