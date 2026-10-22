package slowbro;

import java.util.List;

/** Provides methods for displaying application messages to the user. */
public class UI {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String TASK_LIST_HEADER =
            " Here are the tasks in your list:";
    private static final String MATCHING_TASKS_HEADER =
            " Here are the matching tasks in your list:";
    private static final String TASK_ADDED_HEADER =
            " Got it. I've added this task:";
    private static final String TASK_DELETED_HEADER =
            " Noted. I have removed this task:";
    private static final String TASK_COUNT_FORMAT =
            " Now you have %d tasks in the list.";
    private static final String INVALID_TASK_NUMBER_MESSAGE =
            " Please provide a valid task number.";
    private static final String OUT_OF_BOUNDS_TASK_NUMBER_MESSAGE =
            " Task number out of range.";
    private static final String INVALID_COMMAND_MESSAGE =
            " Sorry, I couldn't understand that command.";
    private static final String INVALID_NUMBER_MESSAGE =
            " Please enter a valid number.";
    private static final String MARKED_DONE_MESSAGE =
            " Nice! I've marked this task as done:";
    private static final String MARKED_NOT_DONE_MESSAGE =
            " OK, I've marked this task as not done yet:";
    private static final String EXIT_MESSAGE =
            "Bye. Hope to see you again soon!";

    /** Displays the application greeting. */
    public static void printGreeting() {
        String banner = "  ____  _               _                \n"
                + " / ___|| | ___         | |__  _ __ ___  \n"
                + " \\___ \\| |/ _ \\ \\ /\\ / / '_ \\| '__/ _ \\\n"
                + "  ___) | | (_) \\ V  V /| |_) | | | (_) |\n"
                + " |____/|_|\\___/ \\_/\\_/ |_.__/|_|  \\___/\n";

        System.out.println(DIVIDER);
        System.out.print(banner);
        System.out.println("Hello! I'm Slowbro.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Displays the application exit message. */
    public static void printExitMessage() {
        System.out.println(DIVIDER);
        System.out.println(EXIT_MESSAGE);
        System.out.println(DIVIDER);
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param tasks the task list to display
     */
    public static void listTasks(TaskList tasks) {
        System.out.println(DIVIDER);
        System.out.println(TASK_LIST_HEADER);
        printTasks(tasks.getTasks());
        System.out.println(DIVIDER);
    }

    /** Displays tasks matching a search keyword. */
    public static void listMatchingTasks(List<Task> matchingTasks) {
        System.out.println(DIVIDER);
        System.out.println(MATCHING_TASKS_HEADER);
        printTasks(matchingTasks);
        System.out.println(DIVIDER);
    }

    /** Prints tasks with one-based display numbering. */
    private static void printTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format(" %d.%s", i + 1, tasks.get(i)));
        }
    }

    /**
     * Displays an error message for an invalid command.
     *
     * @param usageMessage the correct command usage message
     */
    public static void printInvalidCommand(String usageMessage) {
        System.out.println(DIVIDER);
        System.out.println(INVALID_COMMAND_MESSAGE);
        System.out.println(usageMessage);
        System.out.println(DIVIDER);
    }

    /**
     * Displays a confirmation after adding a task.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks currently in the list
     */
    public static void printAddTask(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println(TASK_ADDED_HEADER);
        System.out.println("   " + task);
        System.out.println(String.format(TASK_COUNT_FORMAT, taskCount + 1));
        System.out.println(DIVIDER);
    }

    /**
     * Displays a confirmation after deleting a task.
     *
     * @param task the task that was deleted
     * @param taskCount the number of tasks remaining in the list
     */
    public static void printDeleteTask(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println(TASK_DELETED_HEADER);
        System.out.println("   " + task);
        System.out.println(String.format(TASK_COUNT_FORMAT, taskCount));
        System.out.println(DIVIDER);
    }

    /** Displays an error message when a task number is outside the valid range. */
    public static void printIndexOutOfBounds() {
        System.out.println(DIVIDER);
        System.out.println(OUT_OF_BOUNDS_TASK_NUMBER_MESSAGE);
        System.out.println(DIVIDER);
    }

    /** Displays an error message when a task number is not a valid number. */
    public static void printInvalidNumber() {
        System.out.println(DIVIDER);
        System.out.println(INVALID_NUMBER_MESSAGE);
        System.out.println(DIVIDER);
    }

    /** Displays an error message when a task number is missing or invalid. */
    public static void printInvalidTaskNumber() {
        System.out.println(DIVIDER);
        System.out.println(INVALID_TASK_NUMBER_MESSAGE);
        System.out.println(DIVIDER);
    }

    /**
     * Displays a confirmation after marking or unmarking a task.
     *
     * @param tasks the task list containing the changed task
     * @param shouldUnmark whether the task was marked as not done
     * @param index the zero-based index of the changed task
     */
    public static void printMarkTask(TaskList tasks, boolean shouldUnmark, int index) {
        System.out.println(DIVIDER);
        if (shouldUnmark) {
            System.out.println(MARKED_NOT_DONE_MESSAGE);
        } else {
            System.out.println(MARKED_DONE_MESSAGE);
        }
        System.out.println(tasks.get(index));
        System.out.println(DIVIDER);
    }
}
