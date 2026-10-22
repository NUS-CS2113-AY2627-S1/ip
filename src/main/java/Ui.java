import java.util.ArrayList;
import java.util.Scanner;

/**
 * Everything the user types and everything the user sees.
 * <p>
 * All reading from the keyboard and all writing to the screen happens here, so
 * that the rest of the program can be read, and one day tested, without any
 * reference to the console. Every message below is part of the bot's butler
 * voice, which AGENTS.md describes; the layout -- a separator closing each
 * exchange, a leading space on anything that went wrong, a leading tab on task
 * lines -- is equally deliberate and is what the text-UI test cases compare
 * against.
 */
public class Ui {

    /** Width of the separator line that closes each exchange. */
    private static final int SEPARATOR_WIDTH = 55;

    /** The name the bot introduces itself by. */
    private static final String NAME = "CortisolBot";

    /** Source of everything the user types. */
    private final Scanner scanner;

    /**
     * Constructs a Ui that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads one line of input.
     *
     * @return the line the user typed, exactly as typed.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        System.out.println("-".repeat(SEPARATOR_WIDTH));
    }

    /**
     * Prints the banner and the opening greeting.
     */
    public void showWelcome() {
        String banner = "  ____           _   _           _ ____        _   \n"
                + " / ___|___  _ __| |_(_)___  ___ | | __ )  ___ | |_ \n"
                + "| |   / _ \\| '__| __| / __|/ _ \\| |  _ \\ / _ \\| __|\n"
                + "| |__| (_) | |  | |_| \\__ \\ (_) | | |_) | (_) | |_ \n"
                + " \\____\\___/|_|   \\__|_|___/\\___/|_|____/ \\___/ \\__|";

        System.out.println(banner);
        showLine();
        System.out.printf("Greetings sir/madam, %s humbly at your service.\n", NAME);
        System.out.println("How may I serve you this evening?");
        showLine();
    }

    /**
     * Prints the closing line of the session.
     */
    public void showFarewell() {
        System.out.println("Tonight has been an honour, sir/madam. "
                + "I shall bid you good evening.");
        showLine();
    }

    /**
     * Reports what was found in the data file when the bot started.
     * <p>
     * Says nothing at all when there was nothing to report, so that a first run
     * opens with the greeting alone.
     *
     * @param taskCount number of tasks that were loaded.
     * @param skippedLineCount number of lines that could not be understood.
     */
    public void showLoadReport(int taskCount, int skippedLineCount) {
        if (skippedLineCount > 0) {
            System.out.printf(" I could not read %d of my records, sir/madam. "
                    + "I have set them aside.%n", skippedLineCount);
        }
        if (taskCount > 0) {
            System.out.printf(" I have retrieved %d of your tasks, sir/madam.%n", taskCount);
        }
        if (skippedLineCount > 0 || taskCount > 0) {
            showLine();
        }
    }

    /**
     * Prints an explanation of something that went wrong.
     *
     * @param message explanation already phrased for the user, as the messages
     *                carried by {@link CortisolException} are.
     */
    public void showError(String message) {
        System.out.println(" " + message);
        showLine();
    }

    /**
     * Lists all tasks currently in the task list.
     *
     * @param tasks list containing the tasks.
     */
    public void showTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your list is presently empty, sir/madam. A rare luxury.");
            showLine();
            return;
        }

        System.out.println("Your tasks, sir/madam, as they presently stand:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("\t%d.%s\n", i + 1, tasks.get(i));
        }
        showLine();
    }

    /**
     * Shows the tasks that matched a search.
     * <p>
     * The matches are numbered from 1 among themselves rather than by their
     * place in the whole list, which is what the course's own example shows.
     * Note the consequence: a number here is not the number `mark` and `delete`
     * expect. Showing the list numbers instead would need the matches to carry
     * their positions, which a plain list of tasks does not.
     *
     * @param matches the tasks that matched, in list order.
     * @param keyword what was searched for, quoted back to the user.
     */
    public void showMatchingTasks(ArrayList<Task> matches, String keyword) {
        if (matches.isEmpty()) {
            System.out.printf("Nothing in your list mentions '%s', sir/madam.\n", keyword);
            showLine();
            return;
        }

        System.out.printf("The following mention '%s', sir/madam:\n", keyword);
        for (int i = 0; i < matches.size(); i++) {
            System.out.printf("\t%d.%s\n", i + 1, matches.get(i));
        }
        showLine();
    }

    /**
     * Announces a newly added task and the resulting size of the list.
     *
     * @param task the task that was just added.
     * @param taskCount number of tasks in the list after the addition.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.printf(
                "Very good, sir/madam. I have added the following:\n\t%s\n"
                        + " That makes %d in your keeping.\n",
                task, taskCount);
        showLine();
    }

    /**
     * Announces a task that has just been marked done.
     *
     * @param task the task that was marked.
     */
    public void showMarkedTask(Task task) {
        System.out.println("Consider it done, sir/madam:");
        System.out.printf("\t%s\n", task);
        showLine();
    }

    /**
     * Announces a task that has just been marked not done.
     *
     * @param task the task that was unmarked.
     */
    public void showUnmarkedTask(Task task) {
        System.out.println("Very well, sir/madam. I have returned it to the undone:");
        System.out.printf("\t%s\n", task);
        showLine();
    }

    /**
     * Announces a removed task and the resulting size of the list.
     *
     * @param removedTask the task that was removed.
     * @param taskCount number of tasks left in the list.
     */
    public void showRemovedTask(Task removedTask, int taskCount) {
        System.out.println("Consider it forgotten, sir/madam:");
        System.out.printf("\t%s\n", removedTask);
        System.out.printf(" That leaves %d in your keeping.\n", taskCount);
        showLine();
    }
}
