package floppy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import floppy.task.Task;

/**
 * Contains the task list and the operations that change it, such as adding,
 * removing and looking up tasks by their position in the list.
 */
public class TaskList {

    /** The tasks, in the order the user added them. */
    private final ArrayList<Task> tasks;

    /** Constructs an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list holding the given tasks, in the same order.
     *
     * @param tasks the tasks to start with, for example the ones loaded from a file.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns how many tasks the list holds.
     *
     * @return the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list holds no tasks.
     *
     * @return true if there are no tasks.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the task at the given position, counting from 0.
     *
     * @param index the position of the task.
     * @return the task at that position.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the given task from the list.
     *
     * @param task the task to remove.
     */
    public void remove(Task task) {
        tasks.remove(task);
    }

    /**
     * Returns the tasks whose description contains the given keyword, ignoring case,
     * in the order they appear in the list.
     *
     * @param keyword the word or phrase to look for.
     * @return the matching tasks, which may be empty.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        String target = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(target)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the tasks as a list that cannot be modified, for classes that only need
     * to read them, such as when saving to file or printing.
     *
     * @return a read-only view of the tasks.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
