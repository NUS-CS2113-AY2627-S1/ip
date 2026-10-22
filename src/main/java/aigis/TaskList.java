package aigis;

import java.util.ArrayList;
import java.util.List;

import aigis.task.Task;

/**
 * Stores and manages the tasks handled by Aigis.
 */
public class TaskList {
    /** Maximum number of tasks that Aigis can store. */
    public static final int MAX_TASKS = 100;

    /** The tasks currently stored by Aigis. */
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return The number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether this list has reached its capacity.
     *
     * @return {@code true} if no more tasks can be added.
     */
    public boolean isFull() {
        return tasks.size() >= MAX_TASKS;
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index The zero-based index of the task.
     * @return The task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task when the list has not reached its capacity.
     *
     * @param task The task to add.
     * @return {@code true} if the task was added, or {@code false} if the list
     *         is full.
     */
    public boolean add(Task task) {
        if (isFull()) {
            return false;
        }
        return tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     *
     * @param index The zero-based index of the task to remove.
     * @return The removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns all tasks whose descriptions contain the specified keyword.
     *
     * @param keyword The text to search for.
     * @return The tasks whose descriptions contain the keyword.
     */
    public List<Task> find(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
