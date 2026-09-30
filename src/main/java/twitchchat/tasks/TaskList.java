package twitchchat.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import twitchchat.exceptions.TwitchChatInvalidTaskIdException;

/** Stores tasks and provides operations on the task collection. */
public class TaskList {

    private final ArrayList<Task> tasks;
    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Creates a task list with the supplied initial tasks.
     *
     * @param initialTasks tasks to place in the list
     */
    public TaskList(List<Task> initialTasks) {
        this.tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Returns an unmodifiable view of the tasks in their current order.
     *
     * @return tasks in the list
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /** Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }
    /**
     * Returns the task with the specified one-based task ID.
     *
     * @param taskId one-based ID of the task to retrieve
     * @return task associated with the specified ID
     * @throws TwitchChatInvalidTaskIdException if the ID is outside the task list range
     */
    public Task getTask(int taskId) {
        if (taskId < 1 || taskId > tasks.size()) {
            throw new TwitchChatInvalidTaskIdException("Invalid task ID");
        }
        return tasks.get(taskId - 1); // 1-indexed to 0-indexed
    }

    /** Returns the number of tasks in the list.
     *
     * @return number of tasks
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Marks the specified task as done.
     *
     * @param taskId one-based ID of the task to mark
     * @return the updated task
     * @throws TwitchChatInvalidTaskIdException if the ID is outside the task list range
     */
    public Task markTask(int taskId) {
        Task task = getTask(taskId);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the specified task as not done.
     *
     * @param taskId one-based ID of the task to unmark
     * @return the updated task
     * @throws TwitchChatInvalidTaskIdException if the ID is outside the task list range
     */
    public Task unmarkTask(int taskId) {
        Task task = getTask(taskId);
        task.markAsNotDone();
        return task;
    }

    /** Prints all tasks with their one-based list positions. */
    public void printTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d.", i + 1);
            tasks.get(i).printTask();
        }
    }

    /**
     * Removes the task with the specified one-based task ID.
     *
     * @param taskId one-based ID of the task to remove
     * @throws TwitchChatInvalidTaskIdException if the ID is outside the task list range
     */
    public void removeTask(int taskId) {
        if (taskId < 1 || taskId > tasks.size()) {
            throw new TwitchChatInvalidTaskIdException("Invalid task ID");
        }
        tasks.remove(taskId - 1); // 1-indexed to 0-indexed
    }

    /**
     * Finds tasks whose descriptions contain the specified keyword,
     * ignoring letter case.
     *
     * @param keyword keyword to search for
     * @return matching tasks in their original order
     */
    public List<Task> findTasks(String keyword) {
        String searchTerm = keyword.toLowerCase(Locale.ROOT);
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskName().toLowerCase(Locale.ROOT).contains(searchTerm)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
