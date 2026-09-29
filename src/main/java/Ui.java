import java.util.List;
import java.util.Scanner;

/**
 * Handles all interaction with the user: reading commands from standard input
 * and showing the chatbot's responses on standard output.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER = " ____  _   _    _    _   _ _____ _____ ____  \n"
        + "/ ___|| | | |  / \\  | | | | ____| ____|  _ \\ \n"
        + "\\___ \\| |_| | / _ \\ | |_| |  _| |  _| | |_) |\n"
        + " ___) |  _  |/ ___ \\|  _  | |___| |___|  _ < \n"
        + "|____/|_| |_/_/   \\_\\_| |_|_____|_____|_| \\_\\\n";

    private final Scanner in = new Scanner(System.in);

    // ---------------------------------------------------------------- Input

    /** Returns false once the user has closed the input (e.g. end of file). */
    public boolean hasNextCommand() {
        return in.hasNextLine();
    }

    /** Reads the next line the user types, without leading or trailing spaces. */
    public String readCommand() {
        return in.nextLine().trim();
    }

    // ---------------------------------------------------------------- General output

    /** Shows the divider line printed above and below each response. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Shows the logo and greeting when the chatbot starts. */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hello, I am Shaheer.\nWhat can I do for you?");
        showLine();
    }

    /** Shows the farewell message when the user exits. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you back!");
    }

    /** Shows a problem with the user's input. */
    public void showError(String message) {
        System.out.println("Hmmmm! " + message);
    }

    /** Shows a problem with loading or saving the task file. */
    public void showStorageError(String message) {
        System.out.println(message);
    }

    // ---------------------------------------------------------------- Task output

    /** Shows every task, numbered from 1. */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        showNumberedTasks(tasks);
    }

    /** Shows the results of a search, numbered from 1, or a message if nothing matched. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("There are no matching tasks in your list.");
            return;
        }
        System.out.println("Here are the matching tasks in your list:");
        showNumberedTasks(matchingTasks);
    }

    /**
     * Confirms that a task was added.
     *
     * @param taskCount the number of tasks in the list after adding.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showTaskMessage("Got it. I've added this task:", task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was removed.
     *
     * @param taskCount the number of tasks left in the list.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showTaskMessage("Noted. I've removed this task:", task);
        showTaskCount(taskCount);
    }

    /** Confirms that a task was marked as done. */
    public void showTaskMarked(Task task) {
        showTaskMessage("Nice! I've marked this task as done:", task);
    }

    /** Confirms that a task was marked as not done. */
    public void showTaskUnmarked(Task task) {
        showTaskMessage("OK, I've marked this task as not done yet:", task);
    }

    /** Prints each task on its own line as "1.[T][ ] read book", "2.…". */
    private void showNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    private void showTaskMessage(String message, Task task) {
        System.out.println(message);
        System.out.println("  " + task);
    }

    private void showTaskCount(int taskCount) {
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
