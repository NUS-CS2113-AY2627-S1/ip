package noah.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import noah.exception.NoahException;

/**
 * Manages tasks and validates the task numbers displayed to the user.
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
     * Creates a task list with the supplied tasks in their current order.
     * Copies the list so later changes to the supplied list do not add or
     * remove tasks here.
     *
     * @param tasks Tasks loaded from storage.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Finds the first task with identical type, description, and scheduling details.
     * Completion status does not affect matching, and the list is not changed.
     *
     * @param candidate Task being considered for addition.
     * @return One-based number of the first duplicate, or zero if none exists.
     */
    public int findDuplicate(Task candidate) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).hasSameDetails(candidate)) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * Removes every task from this list, including completed tasks.
     * The caller is responsible for saving the empty list.
     *
     * @return Number of tasks removed, or zero if the list was already empty.
     */
    public int clear() {
        int removedCount = tasks.size();
        tasks.clear();
        return removedCount;
    }

    /**
     * Deletes the task selected by its displayed number.
     *
     * @param taskNumber One-based task number shown to the user.
     * @return Task that was removed.
     * @throws NoahException If the task number is invalid.
     */
    public Task delete(int taskNumber) throws NoahException {
        return tasks.remove(toIndex(taskNumber));
    }

    /**
     * Marks the selected task as done.
     *
     * @param taskNumber One-based task number shown to the user.
     * @return Task after its completion status was updated.
     * @throws NoahException If the task number is invalid.
     */
    public Task mark(int taskNumber) throws NoahException {
        Task task = tasks.get(toIndex(taskNumber));
        task.markAsDone();
        return task;
    }

    /**
     * Marks the selected task as not done.
     *
     * @param taskNumber One-based task number shown to the user.
     * @return Task after its completion status was updated.
     * @throws NoahException If the task number is invalid.
     */
    public Task unmark(int taskNumber) throws NoahException {
        Task task = tasks.get(toIndex(taskNumber));
        task.unmarkAsDone();
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
     * Returns a snapshot for display or saving whose list cannot be modified.
     * The task objects themselves are shared with this task list.
     *
     * @return Tasks in their displayed order.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Finds matching descriptions without changing the list or completion states.
     * Keeps the original task numbers so results work with mark, unmark, and delete.
     *
     * @param keyword Nonblank literal text to find, ignoring letter case.
     * @return Unmodifiable snapshot of matching task numbers and tasks in list order.
     */
    public Map<Integer, Task> find(String keyword) {
        Map<Integer, Task> matches = new LinkedHashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.containsKeyword(keyword)) {
                matches.put(i + 1, task);
            }
        }
        return Collections.unmodifiableMap(matches);
    }

    /**
     * Converts a displayed task number to a valid list index.
     *
     * @param taskNumber One-based task number shown to the user.
     * @return Zero-based index of the selected task.
     * @throws NoahException If the task number is non-positive or out of range.
     */
    private int toIndex(int taskNumber) throws NoahException {
        if (taskNumber <= 0) {
            throw new NoahException("Oops, task numbers start at 1! Use a whole number from list.");
        }
        if (taskNumber > tasks.size()) {
            throw new NoahException("No task numbered " + taskNumber
                    + " on the board! Type list to check your task numbers.");
        }
        return taskNumber - 1;
    }
}
