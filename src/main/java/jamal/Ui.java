package jamal;

import java.util.List;
import java.util.Scanner;

import jamal.task.Task;

/**
 * Handles all interaction with the user: reading commands and printing replies.
 * Every reply is wrapped between two dividers so the output looks consistent.
 */
public class Ui {
    /** Name shown to the user in the greeting. */
    private static final String NAME = "JamaL";

    /** Horizontal line used to visually separate the chatbot's messages. */
    private static final String DIVIDER = "____________________________________________________________";

    /** ASCII art banner displayed on startup. */
    private static final String BANNER = "     ____.                     .____     \n"
            + "    |    |____    _____ _____  |    |    \n"
            + "    |    \\__  \\  /     \\\\__  \\ |    |    \n"
            + "/\\__|    |/ __ \\|  Y Y  \\/ __ \\|    |___ \n"
            + "\\________(____  /__|_|  (____  /_______ \\\n"
            + "              \\/      \\/     \\/        \\/";

    private final Scanner in;

    /** Creates a Ui that reads from standard input. */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /**
     * Reads the next line typed by the user.
     * If input has ended (e.g. input was piped in from a file), this behaves
     * as if the user typed "bye", so the app exits cleanly instead of crashing.
     *
     * @return The line typed by the user, without surrounding whitespace.
     */
    public String readCommand() {
        if (!in.hasNextLine()) {
            return "bye";
        }
        return in.nextLine().trim();
    }

    /** Prints the greeting banner and prompt. */
    public void showWelcome() {
        showMessage(BANNER, "It's " + NAME + ".", "What do you want.");
    }

    /** Prints the exit message. */
    public void showGoodbye() {
        showMessage("alright.");
    }

    /**
     * Prints the given lines wrapped between two dividers.
     *
     * @param lines Lines to print inside the block.
     */
    public void showMessage(String... lines) {
        System.out.println(DIVIDER);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints an error message in the same block format as any other reply.
     *
     * @param message Text of the error.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Prints all tasks, numbered starting from 1.
     *
     * @param tasks Tasks to print.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            showMessage("You got no tasks.");
            return;
        }
        List<Task> taskItems = tasks.asList();
        String[] lines = new String[taskItems.size()];
        for (int i = 0; i < taskItems.size(); i++) {
            lines[i] = (i + 1) + "." + taskItems.get(i);
        }
        showMessage(lines);
    }

    /**
     * Prints the tasks matching a find command, numbered starting from 1.
     *
     * @param matches Tasks that matched the keyword.
     */
    public void showFoundTasks(List<Task> matches) {
        if (matches.isEmpty()) {
            showMessage("nothing matches.");
            return;
        }
        String[] lines = new String[matches.size() + 1];
        lines[0] = "found these:";
        for (int i = 0; i < matches.size(); i++) {
            lines[i + 1] = (i + 1) + "." + matches.get(i);
        }
        showMessage(lines);
    }
}
