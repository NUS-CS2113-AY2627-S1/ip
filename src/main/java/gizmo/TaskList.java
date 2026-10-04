package gizmo;

import gizmo.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns Gizmo's collection of tasks and operations on that collection.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Creates a task list containing the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a one-based task number.
     *
     * @throws GizmoException if the task number does not identify a task
     */
    public Task getTask(int taskNumber) throws GizmoException {
        checkTaskNumber(taskNumber);
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns the task at a one-based task number.
     *
     * @throws GizmoException if the task number does not identify a task
     */
    public Task deleteTask(int taskNumber) throws GizmoException {
        checkTaskNumber(taskNumber);
        return tasks.remove(taskNumber - 1);
    }

    /** Returns the number of tasks currently stored. */
    public int getSize() {
        return tasks.size();
    }

    /** Returns whether the task list contains no tasks. */
    public boolean emptyCheck() {
        return tasks.isEmpty();
    }

    /**
     * Returns a read-only view for display and storage.
     *
     * @return an unmodifiable task list
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /** Validates a one-based task number before accessing the backing list. */
    private void checkTaskNumber(int taskNumber) throws GizmoException {
        if (taskNumber < 1) {
            throw new GizmoException("The task number must be a positive integer!");
        }
        if (taskNumber > tasks.size()) {
            throw new GizmoException(
                    "Failed: you can't mark/unmark/delete a task that doesn't exist :/");
        }
    }

    /**
     * Returns tasks whose descriptions contain the supplied keyword.
     *
     * @param keyword the text to search for.
     * @return matching tasks in their original order.
     */
    public List<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }

        return Collections.unmodifiableList(matchingTasks);
    }
}
