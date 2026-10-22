package gpt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * Stores and manages the user's tasks.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the given index.
     *
     * @param index zero-based index of the task
     * @return deleted task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks and returns the task at the given index as done.
     *
     * @param index zero-based index of the task
     * @return marked task
     */
    public Task mark(int index) {
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks and returns the task at the given index as not done.
     *
     * @param index zero-based index of the task
     * @return unmarked task
     */
    public Task unmark(int index) {
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns tasks whose descriptions contain the given keyword.
     * The search is case-insensitive.
     *
     * @param keyword keyword to search for
     * @return matching tasks
     */
    public TaskList find(String keyword) {
        TaskList matchingTasks = new TaskList();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        for (Task task : tasks) {
            String description = task.getDescription().toLowerCase(Locale.ROOT);
            if (description.contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
