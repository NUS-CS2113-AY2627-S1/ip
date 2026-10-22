package dawn.task;

import dawn.exception.DawnException;

import java.util.ArrayList;

/**
 * Encapsulates the operations and state of the task collection.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Retrieves the current number of tasks stored.
     *
     * @return the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves the task at the specified zero-based index.
     *
     * @param index the position of the task.
     * @return the task at the given index.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Appends a new task to the end of the collection.
     *
     * @param task the concrete task to add.
     * @throws DawnException if the list exceeds the maximum storage capacity.
     */
    public void addTask(Task task) throws DawnException {
        if (tasks.size() >= MAX_TASKS) {
            throw new DawnException("I can keep at most " + MAX_TASKS + " tasks in your list.");
        }
        tasks.add(task);
    }

    /**
     * Inserts a task at its original position when a failed save is rolled back.
     *
     * @param index the position at which to restore the task.
     * @param task the task to restore.
     * @throws DawnException if the list is already at capacity.
     */
    public void insertTask(int index, Task task) throws DawnException {
        if (tasks.size() >= MAX_TASKS) {
            throw new DawnException("I can keep at most " + MAX_TASKS + " tasks in your list.");
        }
        tasks.add(index, task);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param index the position of the task to drop.
     * @return the removed task.
     */
    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Finds and returns all tasks whose descriptions contain the specified keyword.
     *
     * @param keyword the keyword to search for.
     * @return a list of matching tasks.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matching = new ArrayList<>();
        for (Task task : tasks) {
            if (task.containsKeyword(keyword)) {
                matching.add(task);
            }
        }
        return matching;
    }
}
