package turing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import turing.task.Task;
import turing.task.TaskList;
import turing.task.TaskTime;

/**
 * Handles everything the chatbot says to the user and everything it hears back.
 * Gathering the wording and the layout of the replies here means the rest of
 * the chatbot can decide what happened without also deciding how to phrase it,
 * and the look of a reply can be changed in one place.
 */
public class Ui {
    /** Banner shown once when the chatbot starts. */
    private static final String BANNER = """
             _____ _   _ ____  ___ _   _  ____
            |_   _| | | |  _ \\|_ _| \\ | |/ ___|
              | | | | | | |_) || ||  \\| | |  _
              | | | |_| |  _ < | || |\\  | |_| |
              |_|  \\___/|_| \\_\\___|_| \\_|\\____|
            """;

    /** Horizontal divider printed around every chatbot response. */
    private static final String DIVIDER = "____________________________________________________________";

    /** Indent placed before a task when a reply shows it on its own line. */
    private static final String TASK_INDENT = "  ";

    /** Source of the lines the user types. */
    private final Scanner scanner = new Scanner(System.in);

    /** Shows the banner and the greeting displayed when the chatbot starts. */
    public void showWelcome() {
        System.out.println(BANNER);
        show("Hello! I'm Turing", "What can I do for you?");
    }

    /**
     * Returns whether the user has typed another line. Input can end without a
     * "bye", for example when a script is piped in, and that ends the session
     * just as politely.
     *
     * @return True if another line is waiting to be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next line the user typed, exactly as they typed it.
     *
     * @return One raw line of input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Shows the parting message. */
    public void showGoodbye() {
        show("Bye. Hope to see you again soon!");
    }

    /**
     * Shows every stored task, numbered from 1 as the user refers to them.
     *
     * @param tasks Tasks to list.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            show("There is nothing in your list yet.");
            return;
        }

        showNumberedTasks("Here are the tasks in your list:", tasks.getTasks());
    }

    /**
     * Shows the tasks a search turned up, numbered from 1. The numbers count
     * the matches rather than naming positions in the full list, which is what
     * the user sees in front of them.
     *
     * @param matchingTasks Tasks the search found.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            show("There are no matching tasks in your list.");
            return;
        }

        showNumberedTasks("Here are the matching tasks in your list:", matchingTasks);
    }

    /**
     * Shows what a given day holds, numbered from 1.
     *
     * @param date Day being asked about.
     * @param tasksOnDate Tasks falling on that day.
     */
    public void showTasksOn(LocalDate date, List<Task> tasksOnDate) {
        String displayedDate = TaskTime.format(date);
        if (tasksOnDate.isEmpty()) {
            show("You have nothing on " + displayedDate + ".");
            return;
        }

        showNumberedTasks("Here is what you have on " + displayedDate + ":", tasksOnDate);
    }

    /**
     * Shows a heading followed by the given tasks, numbered from 1.
     *
     * @param heading Line introducing the tasks.
     * @param tasks Tasks to show, in the order they should appear.
     */
    private static void showNumberedTasks(String heading, List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        lines.add(heading);

        int taskNumber = 1;
        for (Task task : tasks) {
            lines.add(taskNumber + "." + task);
            taskNumber++;
        }
        // show takes the lines one by one, so hand it an array of them.
        show(lines.toArray(new String[0]));
    }

    /**
     * Confirms that a task has been added.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks stored after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        show("Got it. I've added this task:", TASK_INDENT + task, describeTaskCount(taskCount));
    }

    /**
     * Confirms that a task has been removed.
     *
     * @param task Task that was removed.
     * @param taskCount Number of tasks stored after the removal.
     */
    public void showTaskRemoved(Task task, int taskCount) {
        show("Noted. I've removed this task:", TASK_INDENT + task, describeTaskCount(taskCount));
    }

    /**
     * Confirms that the done status of a task has changed.
     *
     * @param task Task whose status changed.
     * @param isDone True if the task is now done, false if it is now not done.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        String confirmation = isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        show(confirmation, TASK_INDENT + task);
    }

    /**
     * Shows how many tasks were read back from the save file, so the user can
     * tell at a glance that nothing was lost since last time.
     *
     * @param taskCount Number of tasks restored.
     */
    public void showTasksRestored(int taskCount) {
        show("Welcome back. I remembered " + taskCount + " tasks from last time.");
    }

    /**
     * Warns that part of the save file could not be understood and was left out.
     *
     * @param skippedLineCount Number of lines that were skipped.
     */
    public void showSkippedSaveLines(int skippedLineCount) {
        show("I could not make sense of " + skippedLineCount + " line(s) in your save file,",
                "so I left them out. Everything else is back in your list.");
    }

    /**
     * Explains a problem to the user in the same shape as any other reply, so
     * that a mistake reads as part of the conversation rather than a crash.
     *
     * @param exception Problem to explain.
     */
    public void showError(TuringException exception) {
        show(exception.getMessageLines());
    }

    /**
     * Returns the line telling the user how many tasks are stored, shown
     * whenever the size of the list changes.
     *
     * @param taskCount Current number of tasks.
     * @return Sentence naming that number.
     */
    private static String describeTaskCount(int taskCount) {
        return "Now you have " + taskCount + " tasks in the list.";
    }

    /**
     * Prints one or more lines wrapped between two dividers, so that every
     * chatbot reply has a consistent look.
     *
     * @param lines Lines of text to show to the user.
     */
    private static void show(String... lines) {
        System.out.println(DIVIDER);
        for (String line : lines) {
            System.out.println(" " + line);
        }
        System.out.println(DIVIDER);
        System.out.println();
    }
}
