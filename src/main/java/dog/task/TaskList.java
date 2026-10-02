package dog.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Owns the ordered task collection and its add, delete, status, and search operations.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from previously loaded tasks.
     *
     * @param loadedTasks Tasks to copy in their existing order.
     */
    public TaskList(List<Task> loadedTasks) {
        tasks = new ArrayList<>(loadedTasks);
    }

    /**
     * Appends a task.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at a validated index.
     *
     * @param index Zero-based task index.
     * @return Removed task.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Updates the completion status of the selected task.
     *
     * @param index Validated zero-based index.
     * @param isDone Whether to mark the task as done.
     * @return Updated task.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
    public Task setDone(int index, boolean isDone) {
        Task task = tasks.get(index);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        return task;
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Current task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable snapshot of the collection for display or saving.
     *
     * @return Tasks in their stored order; the task objects themselves are shared.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Finds case-insensitive substring matches in descriptions only.
     *
     * @param keyword Nonempty search text.
     * @return Matching tasks in their stored order.
     * @throws IllegalArgumentException If the keyword is blank.
     */
    public List<Task> find(String keyword) {
        if (keyword.isBlank()) {
            throw new IllegalArgumentException("Please provide a keyword, for example: find book");
        }
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
