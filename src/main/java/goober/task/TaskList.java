package goober.task;
import java.util.ArrayList;

/**
 * Stores and manages the tasks in the application.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the end of the task list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the specified index.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Removes the task at the specified zero-based index.
     *
     * @param index Zero-based task index.
     */
    public void deleteTask(int index) {
        tasks.remove(index);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return Current task count.
     */
    public int getTaskCount() {
        return tasks.size();
    }
}
