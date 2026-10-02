package glados;

import glados.task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Holds the user's tasks in order, with operations to add, remove, and look them up.
 *
 * <p>Tasks are stored in an ArrayList, which grows as needed, so there is no
 * fixed task limit or separate count to track. Indexes are 0-based.
 */
public class TaskList {
    /** The tasks, in the order they were added. */
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list holding the given tasks, e.g. ones loaded from the data file.
     *
     * <p>The tasks are copied into a new list, so later changes to the given list
     * do not affect this one.
     *
     * @param tasks the tasks to start with, in order.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at the given index and returns it.
     * Every later task moves up by one.
     *
     * @param index 0-based position of the task.
     * @return the task removed.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given 0-based index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns how many tasks are in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks whose description contains the keyword, in list order.
     * Upper and lower case are treated as the same, so "BOOK" finds "read book".
     */
    public TaskList find(String keyword) {
        // Locale.ROOT makes the case change behave the same whatever language the computer uses.
        String lowerCaseKeyword = keyword.toLowerCase(Locale.ROOT);
        TaskList matchingTasks = new TaskList();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(lowerCaseKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns the tasks that fall on the given date, in list order,
     * e.g. deadlines due that day and events running across it.
     */
    public TaskList getTasksOn(LocalDate date) {
        TaskList tasksOnDate = new TaskList();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                tasksOnDate.add(task);
            }
        }
        return tasksOnDate;
    }
}
