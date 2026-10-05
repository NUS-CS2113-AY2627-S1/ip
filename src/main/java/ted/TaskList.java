package ted;

import java.util.ArrayList;
import java.util.List;

import ted.exception.TedException;
import ted.task.Task;

/**
 * Owns Ted's task collection and its list operations.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this(List.of());
    }

    /**
     * Creates a task list containing the tasks loaded from storage.
     *
     * @param savedTasks The tasks loaded from storage.
     */
    public TaskList(List<Task> savedTasks) {
        tasks = new ArrayList<>(savedTasks);
    }

    /**
     * Returns a copy of the list for display or storage.
     */
    public List<Task> getAll() {
        return List.copyOf(tasks);
    }

    /**
     * Finds tasks whose descriptions contain a literal, case-sensitive keyword.
     *
     * @param keyword The text to search for in task descriptions.
     * @return The matching tasks in their existing list order.
     */
    public List<Task> find(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return List.copyOf(matchingTasks);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index The zero-based index of the task.
     * @return The removed task.
     * @throws TedException If the index is outside the list.
     */
    public Task remove(int index) throws TedException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Sets the completion status of a task at a zero-based index.
     *
     * @param index The zero-based index of the task.
     * @param isDone Whether the task should be marked done.
     * @return The updated task.
     * @throws TedException If the index is outside the list.
     */
    public Task setDone(int index, boolean isDone) throws TedException {
        checkIndex(index);
        Task task = tasks.get(index);
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmarkAsDone();
        }
        return task;
    }

    private void checkIndex(int index) throws TedException {
        if (index < 0 || index >= tasks.size()) {
            throw new TedException("That task number is not in the list.");
        }
    }
}
