package nga;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Stores tasks and translates user-facing one-based task numbers into list indexes. */
public class TaskList {
    private static final int MAXIMUM_TASKS = 100;
    private final List<Task> tasks = new ArrayList<>();

    /** Creates an empty task list. */
    public TaskList() {
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return the number of tasks currently stored
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list cannot accept another task.
     *
     * @return {@code true} when the maximum task count has been reached
     */
    public boolean isFull() {
        return tasks.size() == MAXIMUM_TASKS;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     * @throws IllegalStateException if the list already contains its maximum number of tasks
     */
    public void add(Task task) {
        if (isFull()) {
            throw new IllegalStateException("The task list is full.");
        }
        tasks.add(task);
    }

    /**
     * Returns the one-based number of the first task with the same details, or -1 if none exists.
     *
     * @param task the task to look up
     * @return the existing task's one-based number, or -1
     */
    public int findDuplicateTaskNumber(Task task) {
        for (int index = 0; index < tasks.size(); index++) {
            if (tasks.get(index).getDuplicateKey().equals(task.getDuplicateKey())) {
                return index + 1;
            }
        }
        return -1;
    }

    /**
     * Returns tasks whose descriptions contain the supplied keyword, ignoring letter case.
     *
     * @param keyword the text to search for in task descriptions
     * @return matching tasks in their original insertion order
     */
    public List<Task> findTasks(String keyword) {
        String searchTerm = keyword.toLowerCase(Locale.ROOT);
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(searchTerm)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns whether the one-based number identifies a stored task.
     *
     * @param taskNumber the user-facing task number to check
     * @return {@code true} when the number identifies a task
     */
    public boolean hasTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Returns the task identified by a valid one-based task number.
     *
     * @param taskNumber the user-facing task number
     * @return the identified task
     * @throws IllegalArgumentException if the number does not identify a task
     */
    public Task getTask(int taskNumber) {
        if (!hasTaskNumber(taskNumber)) {
            throw new IllegalArgumentException("Invalid task number.");
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns the task identified by a valid one-based task number.
     *
     * @param taskNumber the user-facing task number
     * @return the removed task
     * @throws IllegalArgumentException if the number does not identify a task
     */
    public Task removeTask(int taskNumber) {
        if (!hasTaskNumber(taskNumber)) {
            throw new IllegalArgumentException("Invalid task number.");
        }
        return tasks.remove(taskNumber - 1);
    }

    /** Removes all stored tasks from the list. */
    public void clear() {
        tasks.clear();
    }
}
