package clanky.task;

import java.util.ArrayList;

/**
 * Represents the user's list of tasks. Wraps an underlying {@link ArrayList}
 * of {@link Task} objects and exposes operations for adding, removing,
 * retrieving, and searching tasks, without exposing the underlying collection
 * directly to callers
 */
public class TaskList {

    private final ArrayList<Task> tasks;

    /**
     * Creates a new, empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList wrapping an existing collection of tasks,
     * used when restoring tasks loaded from storage.
     *
     * @param tasks the existing list of tasks to wrap.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index the zero-based index of the task to remove.
     * @return the task that was removed.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index the zero-based index of the task to retrieve.
     * @return the task at the given index.
     */
    public Task get(int index){
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list currently contains no tasks.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the underlying list of tasks as a plain {@link ArrayList},
     * primarily for handing off to {@code Storage} for saving to disk.
     *
     * @return the underlying list of tasks.
     */
    public ArrayList<Task> asArrayList() {
        return tasks;
    }

    /**
     * Searches for tasks whose description contains the given keyword.
     *
     * @param keyword the keyword to search for within task descriptions.
     * @return a new list containing only the tasks that matched; this is a
     * snapshot, not a live view, so changes to it do not affect the underlying
     * task list.
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
