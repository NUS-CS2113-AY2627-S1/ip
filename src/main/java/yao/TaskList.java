package yao;

import java.util.ArrayList;
import yao.task.Task;

/**
 * Represents the list of tasks recorded by the user.
 * Encapsulates operations for adding, removing, and accessing tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

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
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
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
     * Removes and returns the task at the specified 0-based index.
     *
     * @param index The 0-based index of the task to remove.
     * @return The removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves the task at the specified 0-based index.
     *
     * @param index The 0-based index of the task to retrieve.
     * @return The task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return The ArrayList of Task objects.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Searches for tasks whose description contains the specified keyword.
     *
     * @param keyword The substring to look for.
     * @return An ArrayList containing all matching Task objects.
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Searches for tasks occurring on the specified date.
     *
     * @param date The date to filter tasks by.
     * @return An ArrayList containing all matching Task objects.
     */
    public ArrayList<Task> findTasksOnDate(java.time.LocalDate date) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isOnDate(date)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
