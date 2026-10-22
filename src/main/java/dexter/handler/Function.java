package dexter.handler;

import dexter.classifications.Command;
import dexter.exceptions.EmptyListException;
import dexter.exceptions.MissingDescriptionException;
import dexter.exceptions.MissingIndexException;
import dexter.tasks.Task;
import dexter.ui.Ui;

import java.util.ArrayList;
import java.util.List;

/** Handles commands that find, mark, unmark, or delete tasks. */
public class Function {

    /**
     * Finds case-sensitive description matches without modifying the task list.
     *
     * @param line the complete find command
     * @param tasks the tasks to search
     * @param ui the user-interface handler used to display the results
     * @throws MissingDescriptionException if the command does not contain a keyword
     */
    public static void find(String line, List<Task> tasks, Ui ui) throws MissingDescriptionException {
        String keyword = line.substring(4).trim();
        if (keyword.isEmpty()) {
            throw new MissingDescriptionException();
        }
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        ui.showMatchingTasks(matches);
    }

    /**
     * Applies a mark, unmark, or delete command to the selected task.
     *
     * @param line the complete command, including the task number
     * @param command the operation to perform
     * @param tasks the task list to modify
     * @param ui the user-interface handler used to display deletion results
     * @throws MissingIndexException if the command does not contain a task number
     * @throws EmptyListException if there are no tasks to modify
     */
    public static void function(String line, Command command, List<Task> tasks, Ui ui)
            throws MissingIndexException, EmptyListException {
        String[] parts = line.split(" ");
        int itemCount = tasks.size();
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new MissingIndexException();
        }
        if (itemCount == 0) {
            throw new EmptyListException();
        }
        int taskNumber;
        try {
            switch (command) {
                case MARK:
                    taskNumber = Integer.parseInt(parts[1]);
                    tasks.get(taskNumber - 1).markAsDone();
                    System.out.println("Alright! Marked it as done!");
                    break;
                case UNMARK:
                    taskNumber = Integer.parseInt(parts[1]);
                    tasks.get(taskNumber - 1).markAsUndone();
                    System.out.println("Alright! I have unchecked the task!");
                    break;
                case DELETE:
                    taskNumber = Integer.parseInt(parts[1]);
                    if (taskNumber > itemCount) {
                        throw new IndexOutOfBoundsException();
                    }
                    Task deletedTask = tasks.get(itemCount - 1);
                    itemCount--;
                    ui.showTaskDeleted(deletedTask, itemCount);
                    tasks.remove(taskNumber - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Oh no! The task number is not valid. Please enter a valid number.");
        }
    }
}
