package mimimeow.ui;

import java.util.List;
import java.util.Scanner;

import mimimeow.task.Task;
import mimimeow.task.TaskList;

/** Handles input and output for the MimiMeow command-line interface. */
public class MimiMeowUi {
    /** Creates a MimiMeow user interface. */
    public MimiMeowUi() {
    }

    /**
     * Reads and trims one line of user input.
     *
     * @param inputScanner scanner connected to user input
     * @return trimmed input line
     */
    public String readUserInput(Scanner inputScanner) {
        return inputScanner.nextLine().trim();
    }

    /** Prints MimiMeow's welcome banner and introductory message. */
    public void showWelcomeMessage() {
        System.out.println(createBanner());
        printWithIndent("Hello! I'm MimiMeow.");
        printWithIndent("What can I do for you?");
        showSeparator();
        printWithIndent("");
    }

    /** Prints the separator used to format the command-line interface. */
    public void showSeparator() {
        printWithIndent("─".repeat(60));
    }

    /** Prints MimiMeow's goodbye message. */
    public void showGoodbyeMessage() {
        printWithIndent("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a user-facing error message in MimiMeow's voice.
     *
     * @param message error message to print
     */
    public void showError(String message) {
        printWithIndent("Miiiision impossible! " + message);
    }

    /**
     * Prints the response after adding a task.
     *
     * @param task added task
     * @param taskCount number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        printWithIndent("(^._.^) meows: Got it! Meow'hv added this task:");
        printWithIndent(task.toString());
        printWithIndent("NOW you have " + taskCount
                + (taskCount == 1 ? " task in the list." : " tasks in the list."));
    }

    /**
     * Prints all tasks currently stored in MimiMeow's task list.
     *
     * @param taskList task list to print
     */
    public void showTaskList(TaskList taskList) {
        if (taskList.size() > 1) {
            printWithIndent("Here are the tasks in your list:");
        } else if (taskList.size() == 1) {
            printWithIndent("Here is the task in your list:");
        } else {
            printWithIndent("There are no tasks in your list yet!");
        }
        for (int i = 0; i < taskList.size(); i++) {
            printWithIndent((i + 1) + ". " + taskList.get(i));
        }
    }

    /**
     * Prints tasks returned by a date-based command.
     *
     * @param tasks tasks to print
     * @param heading heading shown when tasks are present
     * @param emptyMessage message shown when no tasks are present
     */
    public void showScheduledTasks(List<Task> tasks, String heading, String emptyMessage) {
        if (tasks.isEmpty()) {
            printWithIndent(emptyMessage);
            return;
        }
        printWithIndent(heading);
        for (Task task : tasks) {
            printWithIndent("- " + task);
        }
    }


    /**
     * Prints the response after changing a task's completion status.
     *
     * @param task updated task
     * @param isDone new completion status
     */
    /** Prints tasks whose descriptions match a search keyword. */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            printWithIndent("There are no matching tasks in your list.");
            return;
        }
        printWithIndent("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            printWithIndent((i + 1) + ". " + tasks.get(i));
        }
    }

    /** Prints the response after changing a task's completion status. */
    public void showTaskStatus(Task task, boolean isDone) {
        printWithIndent(isDone
                ? "Nice! Meow've marked this task as done:"
                : "OK, Meow've marked this task as not done yet:");
        printWithIndent(task.toString());
    }

    /**
     * Prints the response after deleting a task.
     *
     * @param task deleted task
     * @param taskCount number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        printWithIndent("Noted! Meow've removed this task:");
        printWithIndent(task.toString());
        printWithIndent("Now you have " + taskCount
                + (taskCount == 1 ? " task in the list." : " tasks in the list."));
    }

    /** Prints one line with the standard indentation used by MimiMeow. */
    private void printWithIndent(String message) {
        System.out.println("    " + message);
    }

    /** Creates the decorative banner displayed when MimiMeow starts. */
    private String createBanner() {
        return
                  "      ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )      \n"
                + " ███╗   ███╗██╗███╗   ███╗██╗███╗   ███╗███████╗ ██████╗ ██╗    ██╗\n"
                + " ████╗ ████║██║████╗ ████║██║████╗ ████║██╔════╝██╔═══██╗██║    ██║\n"
                + " ██╔████╔██║██║██╔████╔██║██║██╔████╔██║█████╗  ██║   ██║██║ █╗ ██║\n"
                + " ██║╚██╔╝██║██║██║╚██╔╝██║██║██║╚██╔╝██║██╔══╝  ██║   ██║██║███╗██║\n"
                + " ██║ ╚═╝ ██║██║██║ ╚═╝ ██║██║██║ ╚═╝ ██║███████╗╚██████╔╝╚███╔███╔╝\n"
                + " ╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚═╝╚═╝     ╚═╝╚══════╝ ╚═════╝  ╚══╝╚══╝ \n"
                + "      ( o.o ) | | (^._.^) | | (｡♥‿♥｡) | | (^._.^) | | ( o.o )      \n";
    }
}
