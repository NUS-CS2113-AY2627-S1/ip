package chillguy.task;

import java.util.ArrayList;
import java.util.List;

import chillguy.exception.ChillguyException;
import chillguy.storage.Storage;

/**
 * Owns the ordered tasks and saves each change, undoing it if storage fails.
 * Keeps persistence and rollback together so callers cannot forget either step.
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    private final Storage storage;

    /**
     * Creates a task list with its own copy of the loaded collection and a save destination.
     */
    public TaskList(List<Task> loadedTasks, Storage storage) {
        this.tasks = new ArrayList<>(loadedTasks);
        this.storage = storage;
    }

    /**
     * Returns the number of tasks, including both completed and incomplete tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a snapshot whose list structure cannot be changed by the caller.
     * Task objects are shared and should only be changed through this task list.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Returns tasks whose descriptions contain the given case-sensitive text, in stored order.
     * Leaves the task list and saved file unchanged.
     *
     * @param keyword Nonempty search text validated by the parser.
     * @return A snapshot of matching tasks with an unmodifiable list structure.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return List.copyOf(matches);
    }

    /**
     * Adds and saves a task, removing it again if saving fails.
     */
    public void add(Task task) throws ChillguyException {
        tasks.add(task);
        try {
            storage.save(tasks);
        } catch (ChillguyException exception) {
            tasks.removeLast();
            throw exception;
        }
    }

    /**
     * Deletes and saves the numbered task, restoring its position if saving fails.
     *
     * @param taskNumber One-based task number as shown by the list command.
     * @return The deleted task for display after a successful save.
     */
    public Task delete(int taskNumber) throws ChillguyException {
        int taskIndex = getTaskIndex(taskNumber);
        Task deletedTask = tasks.remove(taskIndex);
        try {
            storage.save(tasks);
        } catch (ChillguyException exception) {
            tasks.add(taskIndex, deletedTask);
            throw exception;
        }
        return deletedTask;
    }

    /**
     * Saves a new completion status, restoring the previous status if saving fails.
     * Leaves storage untouched when the task already has the requested status.
     *
     * @param taskNumber One-based task number as shown by the list command.
     * @param isDone Whether the task should be marked as done.
     * @return The updated task for display after a successful change.
     */
    public Task updateStatus(int taskNumber, boolean isDone) throws ChillguyException {
        Task task = tasks.get(getTaskIndex(taskNumber));
        boolean wasDone = task.isDone();
        if (wasDone == isDone) {
            return task;
        }
        setTaskStatus(task, isDone);
        try {
            storage.save(tasks);
        } catch (ChillguyException exception) {
            setTaskStatus(task, wasDone);
            throw exception;
        }
        return task;
    }

    /**
     * Changes a task's in-memory completion status without saving, including during rollback.
     */
    private void setTaskStatus(Task task, boolean isDone) {
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }

    /**
     * Checks list-dependent constraints before converting to a zero-based index.
     */
    private int getTaskIndex(int taskNumber) throws ChillguyException {
        if (tasks.isEmpty()) {
            throw new ChillguyException("Your task list is empty. Add a task first with todo DESCRIPTION.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new ChillguyException("Please choose a task number from 1 to " + tasks.size()
                    + ". Use list to see them.");
        }
        return taskNumber - 1;
    }
}
