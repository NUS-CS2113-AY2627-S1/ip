package floppy;

import java.util.List;
import java.util.Scanner;

import floppy.task.Task;

/**
 * Deals with interactions with the user: reads the commands they type and prints
 * everything Floppy says back. Keeping all of this in one class means the wording
 * and layout of Floppy's replies live in a single place.
 */
public class Ui {

    /** Divider printed above and below every block of Floppy's output. */
    private static final String HORIZONTAL_LINE =
            "    ____________________________________________________________";

    /** Indentation placed in front of every line Floppy speaks. */
    private static final String INDENT = "     ";

    /** Deeper indentation used when a line shows the detail of the line above it. */
    private static final String INDENT_DETAIL = INDENT + "  ";

    /** ASCII art shown once at startup. */
    private static final String BANNER =
            " _____ _                      \n"
            + "|  ___| | ___  _ __  _ __  _   _\n"
            + "| |_  | |/ _ \\| '_ \\| '_ \\| | | |\n"
            + "|  _| | | (_) | |_) | |_) | |_| |\n"
            + "|_|   |_|\\___/| .__/| .__/ \\__, |\n"
            + "              |_|   |_|    |___/";

    /**
     * Drive noises used to introduce each stored task. Floppy cycles through them
     * so that repeated commands do not produce identical replies, which makes the
     * chatbot feel more alive than a single fixed prefix would.
     */
    private static final String[] DRIVE_NOISES = {
        "*whirr-click*",
        "*chk-chk-chk*",
        "*seeking track 00*",
        "*clunk... spinning up*"
    };

    /** Where the user's commands are read from. */
    private final Scanner in;

    /** Constructs a user interface that reads commands from standard input. */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /**
     * Returns a task count with the right singular or plural noun,
     * for example "1 task" or "5 tasks".
     *
     * @param taskCount the number of tasks to describe.
     * @return the count followed by the correctly pluralised word "task".
     */
    public static String describeCount(int taskCount) {
        String noun = taskCount == 1 ? " task" : " tasks";
        return taskCount + noun;
    }

    /**
     * Returns whether the user has another command waiting.
     * This is false once the input runs out, which happens when input is piped in
     * from a file rather than typed.
     *
     * @return true if another command can be read.
     */
    public boolean hasNextCommand() {
        return in.hasNextLine();
    }

    /**
     * Returns the next command the user typed, without surrounding spaces.
     *
     * @return the command line the user entered.
     */
    public String readCommand() {
        return in.nextLine().strip();
    }

    /** Prints the banner and welcome message shown when Floppy starts up. */
    public void showGreeting() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.println(INDENT + "*click... whirr... clunk*");
        System.out.println(INDENT + "Hello! I'm Floppy, 1.44 MB of pure determination.");
        System.out.println(INDENT + "Tell me a task and I'll hold onto it:");
        System.out.println(INDENT_DETAIL + "todo borrow book");
        System.out.println(INDENT_DETAIL + "deadline return book /by 2019-10-15");
        System.out.println(INDENT_DETAIL + "event project meeting /from Mon 2pm /to 4pm");
        System.out.println(INDENT + "Then 'list', 'mark 1', 'unmark 1', 'delete 1',");
        System.out.println(INDENT + "'find book', or 'bye'.");
        System.out.println(INDENT + "What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Tells the user how many tasks were restored from the previous run.
     *
     * @param taskCount how many tasks were loaded.
     */
    public void showTasksLoaded(int taskCount) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + "*reading sector 0... found you* Welcome back! I kept "
                + describeCount(taskCount) + " safe for you. Type 'list' to see them.");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Confirms that a task has been stored, and says how many tasks are now held.
     *
     * @param task      the task that was just stored.
     * @param taskCount how many tasks Floppy holds after this one was added; also picks the drive noise.
     */
    public void showTaskAdded(Task task, int taskCount) {
        String noise = DRIVE_NOISES[(taskCount - 1) % DRIVE_NOISES.length];
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + noise + " Got it. I've added this task:");
        System.out.println(INDENT_DETAIL + task);
        System.out.println(INDENT + "Now you have " + describeCount(taskCount) + " in the list.");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Confirms that a task has been removed, and says how many tasks remain.
     *
     * @param task      the task that was just removed.
     * @param taskCount how many tasks Floppy holds after the removal.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + "*bzzt, sector wiped* Noted. I've removed this task:");
        System.out.println(INDENT_DETAIL + task);
        System.out.println(INDENT + "Now you have " + describeCount(taskCount) + " in the list.");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Reports that a task is now done.
     *
     * @param task the task that was marked.
     */
    public void showTaskMarked(Task task) {
        showStatusChanged("*clack* Nice! I've marked this task as done:", task);
    }

    /**
     * Reports that a task is no longer done.
     *
     * @param task the task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        showStatusChanged("*rewinds* OK, I've marked this task as not done yet:", task);
    }

    /**
     * Prints every task Floppy is holding, numbered from 1, or says the list is
     * empty if there is nothing to show.
     *
     * @param tasks the tasks Floppy is holding.
     */
    public void showTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            showEmptyList();
            return;
        }

        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + "*rattling through the index*");
        System.out.println(INDENT + "Here are the tasks in your list:");
        showNumberedTasks(tasks);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Prints the tasks that matched a search, numbered from 1, or says that nothing
     * matched.
     *
     * @param matches the tasks whose descriptions contain the keyword.
     * @param keyword the keyword that was searched for.
     */
    public void showMatchingTasks(List<Task> matches, String keyword) {
        System.out.println(HORIZONTAL_LINE);
        if (matches.isEmpty()) {
            System.out.println(INDENT + "*scans every track* Nothing in here matches '"
                    + keyword + "'.");
        } else {
            System.out.println(INDENT + "*seeking '" + keyword + "'*");
            System.out.println(INDENT + "Here are the matching tasks in your list:");
            showNumberedTasks(matches);
        }
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Prints the given tasks numbered from 1, without any surrounding dividers.
     *
     * @param tasks the tasks to print.
     */
    private void showNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(INDENT + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Reports that Floppy could not carry out a command.
     *
     * @param explanation what went wrong, in Floppy's own words.
     */
    public void showProblem(String explanation) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + "*stutters* " + explanation);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Responds to an empty line. Storing a blank task would clutter the list,
     * so Floppy stays in character and ignores it instead.
     */
    public void showBlankInput() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + "*reads an empty sector* ...that was a whole lot of nothing.");
        System.out.println(HORIZONTAL_LINE);
    }

    /** Prints the farewell message shown when the user exits. */
    public void showFarewell() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + "*spinning down... ejecting*");
        System.out.println(INDENT + "Bye. Hope to see you again soon!");
        System.out.println(INDENT + "Please don't leave me in a drawer for another 20 years.");
        System.out.println(HORIZONTAL_LINE);
    }

    /** Prints Floppy's reply when there are no tasks to show. */
    private void showEmptyList() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + "*spins, finds nothing* Not a single byte in here yet.");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Reports that a task changed its done status.
     *
     * @param message the sentence describing what happened.
     * @param task    the task whose status changed.
     */
    private void showStatusChanged(String message, Task task) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(INDENT + message);
        System.out.println(INDENT_DETAIL + task);
        System.out.println(HORIZONTAL_LINE);
    }
}
