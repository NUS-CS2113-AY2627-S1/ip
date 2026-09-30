package jude;

import java.util.ArrayList;
import java.util.List;
import jude.task.Task;

/**
 * Represents a list of tasks.
 * Provides operations to manage the tasks in the list.
 */
public class TaskList {
    private List<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the given list of tasks.
     *
     * @param loadedTasks The list of tasks to initialize the TaskList with.
     */
    public TaskList(List<Task> loadedTasks) {
        this.tasks = loadedTasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to be added.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task from the list by its index.
     *
     * @param index The index of the task to be deleted (0-indexed).
     * @return The task that was deleted.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the list by its index.
     *
     * @param index The index of the task to retrieve (0-indexed).
     * @return The task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list contains no tasks, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}