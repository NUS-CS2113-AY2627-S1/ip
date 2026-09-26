package moistbot.task;

import moistbot.exception.MoistBotException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a dynamically sized list of tasks stored in memory.
 * Provides functionality to add, retrieve, and delete tasks.
 */
public final class TaskManager {
    private final ArrayList<Task> taskList = new ArrayList<>();

    /**
     * Creates an empty task list for one MoistBot application instance.
     */
    public TaskManager() {
    }

    /**
     * Adds a new todo task to the task list.
     *
     * @param description The description of the todo task
     * @return The task that was added
     */
    public Task addTodo(String description) {
        return addTask(new Todo(description));
    }

    /**
     * Adds a new deadline task to the task list.
     *
     * @param description The description of the deadline task
     * @param deadlineDate The deadline date
     * @param deadlineTime The optional deadline time
     * @return The task that was added
     */
    public Task addDeadline(String description, LocalDate deadlineDate, LocalTime deadlineTime) {
        return addTask(new Deadline(description, deadlineDate, deadlineTime));
    }

    /**
     * Adds a new event task to the task list.
     *
     * @param description The description of the event
     * @param startDate The event start date
     * @param startTime The optional event start time
     * @param endDate The event end date
     * @param endTime The optional event end time
     * @return The task that was added
     */
    public Task addEvent(String description, LocalDate startDate, LocalTime startTime,
            LocalDate endDate, LocalTime endTime) {
        return addTask(new Event(description, startDate, startTime, endDate, endTime));
    }

    /**
     * Stores a task in the dynamically sized task list.
     *
     * @param task The task to store
     * @return The task that was stored
     */
    private Task addTask(Task task) {
        taskList.add(task);
        return task;
    }

    /**
     * Deletes and returns a task using its 1-based list number.
     *
     * @param id The 1-based task number
     * @return The task removed from the list
     */
    public Task deleteTask(int id) {
        return taskList.remove(id - 1);
    }

    /**
     * Replaces the current task list with tasks restored from storage.
     *
     * @param tasks The complete collection of tasks to store
     */
    public void setTasks(List<Task> tasks) {
        taskList.clear();
        taskList.addAll(tasks);
    }

    /**
     * Removes the most recently added task when its persistence operation fails.
     */
    public void removeLastTask() {
        if (taskList.isEmpty()) {
            return;
        }
        taskList.remove(taskList.size() - 1);
    }

    /**
     * Restores a removed task at its original 1-based list position.
     *
     * @param id The original 1-based task number
     * @param task The task to restore
     */
    public void restoreTask(int id, Task task) {
        taskList.add(id - 1, task);
    }

    /**
     * Retrieves a task from the list by its 1-based index.
     * Returns null if the index is outside the valid range.
     *
     * @param id The 1-based task index (1 refers to the first task)
     * @return The task at the given index, or null if the index is out of bounds
     */
    public Task getTask(int id) {
        if (id < 1 || id > taskList.size()) {
            return null;
        }
        return taskList.get(id - 1);
    }

    /**
     * Retrieves a task and explains how to correct an invalid task number.
     *
     * @param taskNumber The 1-based number supplied by the user
     * @param commandName The command being executed
     * @return The task identified by the supplied number
     * @throws MoistBotException if the task number does not identify an existing task
     */
    public Task getExistingTask(int taskNumber, String commandName) throws MoistBotException {
        int taskCount = getSize();
        if (taskCount == 0) {
            throw new MoistBotException("My apologies, but I cannot " + commandName
                    + " a task because your task list is empty. Please add a task first, then use '"
                    + commandName + " <task number>'.");
        }
        if (taskNumber < 1) {
            throw new MoistBotException("Please provide a task number of at least 1. Use 'list' to view the "
                    + "available task numbers.");
        }
        if (taskNumber > taskCount) {
            throw new MoistBotException("My apologies, but task " + taskNumber + " does not exist. Please choose a "
                    + "number from 1 to " + taskCount + ". Use 'list' to view the tasks.");
        }
        return getTask(taskNumber);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return The count of tasks stored in the list
     */
    public int getSize() {
        return taskList.size();
    }
}
