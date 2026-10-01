package proton.task;

import proton.exception.ProtonException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns the ordered tasks and supports restoring changes rejected by storage.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this(List.of());
    }

    /**
     * Copies the supplied list's membership, retaining its task objects and order.
     */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Adds a task at the end of the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a live view for display and storage that prevents membership changes.
     * The task objects themselves remain mutable.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword, preserving list order.
     */
    public List<Task> find(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Marks the task identified by a one-based number as done and returns it.
     *
     * @throws ProtonException If the number does not identify an existing task.
     */
    public Task mark(int taskNumber) throws ProtonException {
        Task task = tasks.get(getTaskIndex(taskNumber));
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task identified by a one-based number as not done and returns it.
     *
     * @throws ProtonException If the number does not identify an existing task.
     */
    public Task unmark(int taskNumber) throws ProtonException {
        Task task = tasks.get(getTaskIndex(taskNumber));
        task.markAsNotDone();
        return task;
    }

    /**
     * Removes and returns the task identified by a one-based number.
     *
     * @throws ProtonException If the number does not identify an existing task.
     */
    public Task delete(int taskNumber) throws ProtonException {
        return tasks.remove(getTaskIndex(taskNumber));
    }

    /**
     * Validates a user-facing task number before converting it to a list index.
     */
    private int getTaskIndex(int taskNumber) throws ProtonException {
        if (tasks.isEmpty()) {
            throw new ProtonException(
                    "Positive charge alert! There are no tasks in Proton's orbit yet.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new ProtonException(
                    "Positive charge alert! Choose a task number from 1 to " + tasks.size() + ".");
        }

        return taskNumber - 1;
    }

    /**
     * Captures membership, order, and completion flags before a command runs.
     */
    public Snapshot createSnapshot() {
        return new Snapshot(tasks);
    }

    /**
     * Restores membership, order, and completion flags from a previous snapshot.
     */
    public void restore(Snapshot snapshot) {
        tasks.clear();
        tasks.addAll(snapshot.tasks);
        for (int i = 0; i < tasks.size(); i++) {
            if (snapshot.completionStatuses.get(i)) {
                tasks.get(i).markAsDone();
            } else {
                tasks.get(i).markAsNotDone();
            }
        }
    }

    /**
     * Keeps rollback details private to TaskList. Completion flags are copied
     * separately because retaining task references alone would not undo marking.
     */
    public static class Snapshot {
        private final List<Task> tasks;
        private final List<Boolean> completionStatuses = new ArrayList<>();

        /**
         * Copies the current membership, order, and completion flags for later restoration.
         */
        private Snapshot(List<Task> tasks) {
            this.tasks = new ArrayList<>(tasks);
            for (Task task : tasks) {
                completionStatuses.add(task.isDone());
            }
        }
    }
}
