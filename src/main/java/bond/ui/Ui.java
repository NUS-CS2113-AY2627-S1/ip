package bond.ui;

import java.util.Scanner;

import bond.task.Task;
import bond.task.TaskList;

/**
 * Handles console input and output for Bond.
 */
public class Ui {

    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "    ____                  __\n"
            + "   / __ )____  ____  ____/ /\n"
            + "  / __  / __ \\/ __ \\/ __  /\n"
            + " / /_/ / /_/ / / / / /_/ /\n"
            + "/_____/\\____/_/ /_/\\__,_/\n";

    private final Scanner scanner;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command can be read from standard input.
     *
     * @return True if input has not ended.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return Next user command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays Bond's welcome message.
     */
    public void showWelcomeMessage() {
        System.out.println(DIVIDER);
        System.out.print(BANNER);
        System.out.println("    Good day! I'm Bond, James Bond.");
        System.out.println("    Agent 007 at your service, what can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Displays the divider between commands and responses.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays Bond's farewell message.
     */
    public void showGoodbyeMessage() {
        System.out.println("    Bye. Hope to embark on a mission again soon!");
    }

    /**
     * Displays all tasks in their stored order.
     *
     * @param taskList Tasks to display.
     */
    public void showTaskList(TaskList taskList) {
        System.out.println("    Here are the missions in your list:");
        for (int i = 0; i < taskList.getSize(); i++) {
            System.out.println("    " + (i + 1) + "." + taskList.getTask(i));
        }
    }

    /**
     * Displays tasks whose descriptions contain the keyword, using their list numbers.
     *
     * @param taskList Tasks to search in their stored order.
     * @param keyword Keyword or phrase to find in task descriptions.
     */
    public void showMatchingTasks(TaskList taskList, String keyword) {
        System.out.println("    Here are the matching missions in your list:");
        int matchCount = 0;
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            if (task.matchesDescription(keyword)) {
                System.out.println("    " + (i + 1) + "." + task);
                matchCount++;
            }
        }
        if (matchCount == 0) {
            System.out.println("    No missions match that keyword.");
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task Task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println("    Nice work, agent! Another mission accomplished!:");
        System.out.println("      " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("    OK, I've marked this mission as not accomplished yet:");
        System.out.println("      " + task);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task Task that was deleted.
     * @param taskCount Number of tasks after the deletion.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        String missionNoun = taskCount == 1 ? "mission" : "missions";
        System.out.println("    Noted. I've removed this mission:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " " + missionNoun + " in the list.");
    }

    /**
     * Displays confirmation that a typed task was added.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        String missionNoun = taskCount == 1 ? "mission" : "missions";
        System.out.println("    Got it. I've added this mission:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " " + missionNoun + " in the list.");
    }

    /**
     * Displays an error caused by invalid user input.
     *
     * @param errorMessage Explanation of the error.
     * @param correction Instruction for correcting the error.
     */
    public void showError(String errorMessage, String correction) {
        System.out.println("    Mission error: " + errorMessage);
        System.out.println("    " + correction);
    }

    /**
     * Closes the input scanner used by this UI.
     */
    public void close() {
        scanner.close();
    }
}
