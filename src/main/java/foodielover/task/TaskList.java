package foodielover.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the list of tasks and provides operations to manipulate and query them.
 */
public class TaskList {
    /** Internal list storing task objects. */
    private final List<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList initialized with an existing list of tasks.
     *
     * @param tasks Initial list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to be added.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     *
     * @param index Zero-based index of the task to remove.
     * @return Removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based index of the task to retrieve.
     * @return Task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list contains no tasks.
     *
     * @return True if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns an unmodifiable view of all tasks in the list.
     *
     * @return Unmodifiable list containing all tasks.
     */
    public List<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns a list of tasks that occur on or are due on the specified date.
     *
     * @param targetDate Date to filter tasks by.
     * @return List of tasks occurring on the target date.
     */
    public List<Task> getTasksOnDate(LocalDate targetDate) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isOnDate(targetDate)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns a list of tasks whose descriptions contain the specified keyword.
     *
     * @param keyword Keyword to search for.
     * @return List of tasks matching the keyword.
     */
    public List<Task> findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.containsKeyword(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
