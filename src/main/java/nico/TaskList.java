package nico;

import nico.tasks.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the list of tasks the user has added, and provides
 * operations to add, retrieve, mark, delete and search them.
 * Task positions used here are 0-based.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the given tasks, e.g. those loaded from the save file.
     *
     * @param tasks The tasks to start with.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list has no tasks.
     *
     * @return True if the list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the task at the given position.
     *
     * @param index 0-based position of the task.
     * @return The task at that position.
     * @throws NicoException If no task exists at that position.
     */
    public Task get(int index) throws NicoException {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Returns a read-only view of all tasks, in the order they were added.
     *
     * @return All tasks in the list.
     */
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(tasks);
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
     * Removes the task at the given position.
     *
     * @param index 0-based position of the task to remove.
     * @return The task that was removed.
     * @throws NicoException If no task exists at that position.
     */
    public Task delete(int index) throws NicoException {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given position as done or not done.
     *
     * @param index 0-based position of the task.
     * @param isDone True to mark the task as done, false to mark it as not done.
     * @return The task that was updated.
     * @throws NicoException If no task exists at that position.
     */
    public Task setDone(int index, boolean isDone) throws NicoException {
        Task task = get(index);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        return task;
    }

    /**
     * Returns the tasks whose description contains the given keyword.
     * The search ignores upper and lower case.
     *
     * @param keyword The text to search for.
     * @return The matching tasks, in list order. Empty if nothing matches.
     */
    public List<Task> find(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(lowerCaseKeyword))
                .toList();
    }

    /**
     * Returns the deadlines and events that fall on the given date.
     *
     * @param date The date to look up.
     * @return The matching tasks, in list order. Empty if nothing matches.
     */
    public List<Task> getTasksOn(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.occursOn(date))
                .toList();
    }

    private void checkIndex(int index) throws NicoException {
        if (index < 0 || index >= tasks.size()) {
            throw new NicoException("That task number doesn't exist. You have "
                    + tasks.size() + " task(s).");
        }
    }
}