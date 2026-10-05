package goatbot.task;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Manages the tasks stored by Goat Bot.
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
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based task index
     * @return task at the specified index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Deletes and returns the task at the specified zero-based index.
     *
     * @param index zero-based task index
     * @return deleted task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return number of stored tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the final task in the list.
     *
     * @return final task
     */
    public Task getLast() {
        return tasks.get(tasks.size() - 1);
    }

    /**
     * Returns tasks occurring on the specified date.
     *
     * @param date date to search for
     * @return task list containing matching tasks
     */
    public TaskList findOn(LocalDate date) {
        TaskList matches = new TaskList();

        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns tasks whose descriptions contain the specified keyword.
     *
     * @param keyword keyword to search for
     * @return task list containing matching tasks
     */
    public TaskList findTasks(String keyword) {
        TaskList matches = new TaskList();

        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
