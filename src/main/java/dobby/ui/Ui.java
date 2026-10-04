package dobby.ui;

import java.util.Scanner;

import dobby.task.Task;
import dobby.task.TaskList;

/**
 * Handles Dobby's interaction with the user through the command line.
 */
public class Ui {

    /** The line printed between Dobby's messages. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** The command-line input used to read user commands. */
    private final Scanner scanner;

    /** Creates a user-interface handler connected to standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays Dobby's welcome banner and opening message.
     */
    public void showWelcome() {
        String banner = "     *        .        *        .        *\n"
                + "      ____          _      _\n"
                + "     |  _ \\   ___  | |__  | |__   _   _\n"
                + "     | | | | / _ \\ | '_ \\ | '_ \\ | | | |\n"
                + "     | |_| || (_) || |_) || |_) || |_| |\n"
                + "     |____/  \\___/ |_.__/ |_.__/  \\__, |\n"
                + "                                  |___/\n"
                + "     .        *        .        *        .";

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello! I'm Dobby, your mildly magical command goblin.");
        System.out.println("What adventure shall we get into today?");
    }

    /**
     * Returns whether another command is available on standard input.
     *
     * @return true when another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command from standard input.
     *
     * @return the next complete command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays one response surrounded by the standard separator.
     *
     * @param message the response to display.
     */
    public void showMessage(String message) {
        System.out.println("    " + SEPARATOR);
        System.out.println("     " + message);
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Displays all stored tasks in the order in which they were entered.
     *
     * @param tasks the task list to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("    " + SEPARATOR);
        System.out.println("     Behold, brave adventurer! Here are your mighty quests:");

        if (tasks.size() == 0) {
            System.out.println("     Your task pouch is empty - add a quest and let the adventure begin!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("     " + (i + 1) + "." + tasks.get(i));
            }
        }

        System.out.println("    " + SEPARATOR);
    }

    /**
     * Displays the confirmation shown after a task is added.
     *
     * @param task the task that was added.
     * @param taskCount the number of tasks now stored.
     */
    public void showTaskAdded(Task task, int taskCount) {
        String questLabel = taskCount == 1 ? "quest" : "quests";

        System.out.println("    " + SEPARATOR);
        System.out.println("     Huzzah! A new quest has joined your magical task scroll:");
        System.out.println("       " + task);
        System.out.println("     The quest scroll now holds " + taskCount + " " + questLabel
                + ". Keep adventuring!");
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Displays the confirmation shown after a task is marked as done.
     *
     * @param task the task that was marked as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("    " + SEPARATOR);
        System.out.println("     Nice! Quest progress unlocked - I've marked this task as done:");
        System.out.println("       " + task);
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Displays the confirmation shown after a task is marked as not done.
     *
     * @param task the task that was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("    " + SEPARATOR);
        System.out.println("     Plot twist! This quest is back on the adventure board -");
        System.out.println("     I've marked this task as not done yet:");
        System.out.println("       " + task);
        System.out.println("    " + SEPARATOR);
    }

    /**
     * Displays the message used when tasks cannot be saved.
     */
    public void showSaveError() {
        showMessage("I couldn't save the quest scroll right now, but your changes remain in this session.");
    }
}
