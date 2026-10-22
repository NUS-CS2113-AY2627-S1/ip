package bob;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Runs the Bob task manager through a text interface.
 */
public class Bob {
    /** Whether task data can be saved safely during the current run. */
    private static boolean isStorageAvailable = true;

    /**
     * Prevents instantiation of the application runner.
     */
    private Bob() {
    }

    /**
     * Loads saved tasks, falling back to an empty list if loading fails.
     *
     * @return Tasks loaded from storage, or an empty list if storage is unavailable.
     */
    private static ArrayList<Task> loadTasks() {
        try {
            return Storage.loadTasks();
        } catch (BobException exception) {
            isStorageAvailable = false;
            System.out.println("Trouble on the trail! I couldn't load your saved tasks: " + exception.getMessage());
            System.out.println("We'll start with an empty task list instead.");
        } catch (IOException exception) {
            isStorageAvailable = false;
            System.out.println("Trouble on the trail! I couldn't read the data file. "
                    + "We'll start with an empty task list.");
        }
        return new ArrayList<>();
    }

    /**
     * Saves the current tasks if storage is available.
     *
     * @param tasks Tasks to save.
     */
    private static void saveTasks(ArrayList<Task> tasks) {
        if (!isStorageAvailable) {
            System.out.println("I couldn't save your tasks. Check the data file and try again, partner.");
            return;
        }

        try {
            Storage.saveTasks(tasks);
        } catch (IOException exception) {
            isStorageAvailable = false;
            System.out.println("I couldn't save your tasks. Check the data file and try again, partner.");
        }
    }

    /**
     * Returns a grammatically correct message describing the number of tasks.
     *
     * @param taskCount Number of tasks in the list.
     * @return Message containing the task count.
     */
    private static String getTaskCountMessage(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        return "You've got " + taskCount + " " + taskWord + " in your saddlebag.";
    }

    /**
     * Starts the text interface.
     *
     * @param args Command-line arguments; not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
        ui.showIntro();
        String userCmd = ui.readCommand();
        ArrayList<Task> tasks = loadTasks();
        while (!userCmd.equals("bye")) {
            try {
                if (userCmd.equals("list")) {
                    ui.showTasklist(tasks);
                } else if (userCmd.equals("find") || userCmd.startsWith("find ")) {
                    String keyword = parser.parseDescription(userCmd, "find");
                    ui.showMatchingTasks(tasks, keyword);
                } else if (userCmd.equals("mark") || userCmd.startsWith("mark ")) {
                    int taskNumber = parser.parseTaskNumber(userCmd, "mark");
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        throw new BobException("That task isn't in your list, partner.");
                    }
                    tasks.get(taskNumber - 1).markAsDone();
                    saveTasks(tasks);
                    System.out.println("Task completed. Nice work, partner: "
                            + tasks.get(taskNumber - 1).description);
                } else if (userCmd.equals("unmark") || userCmd.startsWith("unmark ")) {
                    int taskNumber = parser.parseTaskNumber(userCmd, "unmark");
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        throw new BobException("That task isn't in your list, partner.");
                    }

                    tasks.get(taskNumber - 1).markAsNotDone();
                    saveTasks(tasks);
                    System.out.println("This task is back on the trail: "
                            + tasks.get(taskNumber - 1).description);
                } else if (userCmd.equals("todo") || userCmd.startsWith("todo ")) {
                    String description = parser.parseDescription(userCmd, "todo");
                    Todo todo = new Todo(description);
                    tasks.add(todo);
                    saveTasks(tasks);

                    System.out.println("Got it, partner. I've added this task:");
                    System.out.println(todo);
                    System.out.println(getTaskCountMessage(tasks.size()));
                } else if (userCmd.equals("deadline") || userCmd.startsWith("deadline ")) {
                    String[] deadlineDetails = parser.parseDeadline(userCmd);
                    Deadline deadline = new Deadline(deadlineDetails[0], deadlineDetails[1]);
                    tasks.add(deadline);
                    saveTasks(tasks);

                    System.out.println("Got it, partner. I've added this task:");
                    System.out.println(deadline);
                    System.out.println("The clock's tickin'! You've got " + deadlineDetails[2] + " days and "
                            + deadlineDetails[3] + " hours left.");
                    System.out.println(getTaskCountMessage(tasks.size()));
                } else if (userCmd.equals("event") || userCmd.startsWith("event ")) {
                    String[] eventDetails = parser.parseEvent(userCmd);
                    Event event = new Event(eventDetails[0], eventDetails[1], eventDetails[2]);
                    tasks.add(event);
                    saveTasks(tasks);

                    System.out.println("Got it, partner. I've added this task:");
                    System.out.println(event);
                    System.out.println(getTaskCountMessage(tasks.size()));
                } else if (userCmd.equals("delete") || userCmd.startsWith("delete ")) {
                    int taskNumber = parser.parseTaskNumber(userCmd, "delete");
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        throw new BobException("That task isn't in your list, partner.");
                    }

                    Task removedTask = tasks.remove(taskNumber - 1);
                    saveTasks(tasks);
                    System.out.println("Consider this task gone, partner:");
                    System.out.println(removedTask);
                    System.out.println(getTaskCountMessage(tasks.size()));
                } else {
                    throw new BobException("I don't recognize that command, partner.");
                }

                System.out.println("----------------------------------------------");
            } catch (BobException exception) {
                ui.showError(exception.getMessage());
            }
            userCmd = ui.readCommand();
        }
        ui.showBye();
        ui.closeScanner();
    }
}
