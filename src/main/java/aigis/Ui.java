package aigis;

import java.util.List;
import java.util.Scanner;

import aigis.task.Task;

/**
 * Handles console input and output for Aigis.
 */
public class Ui implements AutoCloseable {
    /** The welcome message shown when Aigis starts. */
    private static final String BANNER =
            """
                          __        __     _______   __      ________ \s
                         /""\\      |" \\   /" _   "| |" \\    /"       )\s
                        /    \\     ||  | (: ( \\___) ||  |  (:   \\___/ \s
                       /' /\\  \\    |:  |  \\/ \\      |:  |   \\___  \\   \s
                      //  __'  \\   |.  |  //  \\ ___ |.  |    __/  \\\\  \s
                     /   /  \\\\  \\  /\\  |\\(:   _(  _|/\\  |\\  /" \\   :) \s
                    (___/    \\___)(__\\_|_)\\_______)(__\\_|_)(_______/  \s

                    Aigis is ready to help!
                    Awaiting commands...
                    """;

    /** The closing message shown when Aigis exits. */
    private static final String CLOSING = """
             ----------------------------------------------------
             Tasks completed. See you again soon!
             __________________________________________________________
             """;

    /** Reads commands from standard input. */
    private final Scanner scanner;

    /**
     * Creates a UI connected to the standard console streams.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the startup banner.
     */
    public void showWelcome() {
        System.out.println(BANNER);
    }

    /**
     * Reads the next command from the user.
     *
     * @return The next input line, or {@code null} when input is exhausted.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /**
     * Displays a single message to the user.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Displays the divider separating command interactions.
     */
    public void showLine() {
        System.out.println("_______");
    }

    /**
     * Displays every task with its one-based number.
     *
     * @param tasks The tasks to display.
     */
    public void showTasks(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            showMessage(i + 1 + ". " + tasks.get(i));
        }
    }

    /**
     * Displays every task in a list with its one-based number.
     *
     * @param tasks The tasks to display.
     */
    public void showTasks(List<Task> tasks) {
        showIndexedTasks(tasks);
    }

    /**
     * Displays a sequence of tasks with one-based numbering.
     *
     * @param tasks The tasks to display.
     */
    private void showIndexedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            showMessage(i + 1 + ". " + tasks.get(i));
        }
    }

    /**
     * Displays the closing message.
     */
    public void showGoodbye() {
        System.out.println(CLOSING);
    }

    /**
     * Closes the console input scanner.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
