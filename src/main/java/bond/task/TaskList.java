package bond.task;

import java.util.ArrayList;

/**
 * Stores the tasks created during a Bond session.
 */
public class TaskList {

    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     *
     * @param index Zero-based position of the task to remove.
     * @return Task removed from the list.
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Restores a task at its previous index after a deletion could not be saved.
     *
     * @param index Zero-based position at which to restore the task.
     * @param task Task to restore.
     */
    public void restoreTask(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based position of the task.
     * @return Task at the specified index.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Removes the last task to roll back an addition that could not be saved.
     */
    public void removeLastTask() {
        if (tasks.isEmpty()) {
            return;
        }

        tasks.remove(tasks.size() - 1);
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of tasks in the list.
     */
    public int getSize() {
        return tasks.size();
    }
}
