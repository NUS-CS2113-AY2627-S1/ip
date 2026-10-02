package nova;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import nova.task.Task;

/**
 * Handles all interaction with the user: reading commands from input
 * and printing all the responses.
 */
public class Ui {

    private static final String DIVIDER =
            "\t____________________________________________________________";
    private static final String CYAN = "\u001B[36m"; // changes colour of banner
    private static final String RESET = "\u001B[0m";

    private static final String LOGO = "\n"
            + "ooooo      ooo  .oooooo.  oooooo     oooo      .o.      \n"
            + "`888b.     `8' d8P'  `Y8b  `888.     .8'      .888.     \n"
            + " 8 `88b.    8 888      888  `888.   .8'      .8\"888.    \n"
            + " 8   `88b.  8 888      888   `888. .8'      .8' `888.   \n"
            + " 8     `88b.8 888      888    `888.8'      .88ooo8888   \n"
            + " 8       `888 `88b    d88'     `888'      .8'     `888. \n"
            + "o8o        `8  `Y8bood8P'       `8'      o88o     o8888o\n";

    private static final String[] GREETINGS = {
            "Ah, look who seeks my counsel once again. How... predictable. "
                    + "What is it this time?",
            "You return. I confess, I did wonder if you'd have the nerve.",
            "Well, well. Look who requires my particular brand of brilliance. "
                    + "Tell me then, what are we doing today?",
            "Ah, you've returned... how disappointing. Very well, what do you want?",
            "Oh great one, how may I assist you on this dreadful occasion?",
            "....And here I'd hoped I wouldn't see you for a time yet. "
                    + "Alright, let's keep this snappy- what do you want?"
    };

    private static final String[] FAREWELLS = {
            "Leaving so soon? I suppose even amateurs need their rest.",
            "Farewell, then. Try not to miss me too terribly.",
            "Ah, fleeing already? Coward. Go on, then.",
            "Until next time. Do try to bring a better question with you.",
            "Leaving? Very well. I have far grander things to attend to anyway.",
            "Goodbye. I'll be here, plotting, as always."
    };

    private final Scanner scanner;
    private final Random random;

    /**
     * reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    /**
     * Reads the next command typed by the user, trimmed of surrounding spaces.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints the divider line used to frame all of Nova's responses.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints the banner and a greeting which is randomly chosen
     */
    public void showWelcome() {
        showDivider();
        System.out.println(CYAN + LOGO + RESET);
        showFramed(pickRandom(GREETINGS));
    }

    /**
     * Prints a randomly chosen farewell.
     */
    public void showGoodbye() {
        showFramed(pickRandom(FAREWELLS));
    }

    /**
     * Prints an error message
     */
    public void showError(String message) {
        showFramed(message);
    }

    /**
     * Tells the user that saved tasks could not be loaded.
     */
    public void showLoadingError() {
        System.out.println("\tCouldn't read your saved tasks. Starting fresh, I suppose.");
    }

    /**
     * Tells user that tasks could not be saved.
     */
    public void showSavingError() {
        System.out.println("\tI couldn't save your tasks. How inconvenient.");
    }

    /**
     * Confirms that a task was added and shows the current total.
     */
    public void showAdded(Task task, int taskCount) {
        showFramed("I've added this to your ever-growing pile:",
                "  " + task,
                "You now have " + taskCount + " tasks. Do try to keep up.");
    }

    /**
     * Confirms that a task was deleted and shows the remaining total.
     */
    public void showDeleted(Task task, int taskCount) {
        showFramed("Noted. I've removed this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was marked as done.
     */
    public void showMarked(int index, Task task) {
        System.out.println("\tHuh. I'm almost proud. Almost.");
        System.out.println("\tYour task has been marked.");
        System.out.println("\t" + (index + 1) + "." + task);
    }

    /**
     * Tells the user the task was already marked as done.
     */
    public void showAlreadyMarked() {
        System.out.println("\tOh, you simpleton, this was already marked as done.");
    }

    /**
     * Confirms that a task unmarked.
     */
    public void showUnmarked(int index, Task task) {
        System.out.println("\tVery well, your task is unmarked.");
        System.out.println("\t" + (index + 1) + "." + task);
    }

    /**
     * Tells the user the task was already unmarked
     */
    public void showAlreadyUnmarked() {
        System.out.println("\tWell, isn't that embarrassing. This was already unmarked.");
    }

    /**
     * Prints every task with its 1-based number, or a remark if there are none.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("\tEmpty. Truly groundbreaking work you've done here. "
                    + "You need to add something in before "
                    + "I can list it out for you, genius.");
            return;
        }

        showDivider();
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\t" + (i + 1) + "." + tasks.get(i));
        }
        showDivider();
    }

    /**
     * Prints the tasks that matched a find command, or a remark if none did.
     */
    public void showFoundTasks(List<Task> matches) {
        showDivider();
        if (matches.isEmpty()) {
            System.out.println("\tNothing matches. Perhaps try spelling it correctly?");
        } else {
            System.out.println("\tHere are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println("\t" + (i + 1) + "." + matches.get(i));
            }
        }
        showDivider();
    }

    /**
     * Prints each line indented, with a divider above and below.
     */
    private void showFramed(String... lines) {
        showDivider();
        for (String line : lines) {
            System.out.println("\t" + line);
        }
        showDivider();
    }

    /**
     * Returns a random element of a given array.
     */
    private String pickRandom(String[] options) {
        return options[random.nextInt(options.length)];
    }
}