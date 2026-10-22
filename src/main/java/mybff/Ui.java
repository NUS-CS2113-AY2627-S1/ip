package mybff;

import java.util.Scanner;

/** Reads console input and displays chatbot responses. */
public class Ui {
    private static final String SEPARATOR =
            "____________________________________________________________";
    private static final String BANNER =
            "     __  ____   ______  ______ ______ \n"
                    + "     |  \\/  \\ \\ / /  _ \\|  ____|  ____|\n"
                    + "     | \\  / |\\ V /| |_) | |__  | |__   \n"
                    + "     | |\\/| | | | |  _ <|  __| |  __|  \n"
                    + "     | |  | | |.| | |_) | |    | |     \n"
                    + "     |_|  |_| |_| |____/|_|    |_|     \n";

    private final Scanner scanner = new Scanner(System.in);

    /** Reports whether another input line is available. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads the next command without surrounding whitespace. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Closes the console input reader. */
    public void close() {
        scanner.close();
    }

    /** Prints the opening line of a response. */
    public void printSeparator() {
        System.out.println("    " + SEPARATOR);
    }

    /** Ends a command response. */
    public void printResponseEnd() {
        printSeparator();
        System.out.println();
    }

    /** Prints the farewell and its closing line. */
    public void printFarewell() {
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("   " + SEPARATOR);
    }

    /** Prints the stored tasks in their original order. */
    public void printTaskList(TaskList tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays matching tasks numbered from one, or an empty result header if none match. */
    public void printMatchingTasks(TaskList matches) {
        System.out.println("     Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matches.get(i));
        }
    }

    /** Confirms a task addition and the new list size. */
    public void printTaskAdded(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /** Confirms the removed task and the remaining list size. */
    public void printTaskDeleted(Task task, int taskCount) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays a command parsing error. */
    public void printError(MyBffException exception) {
        System.out.println("     OOPS!!! " + exception.getMessage());
    }

    /** Reports an invalid task number. */
    public void printInvalidTaskNumber() {
        System.out.println("     Invalid task number.");
    }

    /** Prints the standard load failure message. */
    public void printLoadError() {
        System.out.println("     OOPS!!! Could not load data/mybff.txt. "
                + "Your saved file has not been changed. Check the file and restart.");
    }

    /** Prints the standard save failure message. */
    public void printSaveError() {
        System.out.println("     OOPS!!! Could not save data/mybff.txt. "
                + "No changes were made. Check the file and try again.");
    }

    /** Confirms the task's updated completion status. */
    public void printCompletionChanged(Task task, boolean isMarkingAsDone) {
        if (isMarkingAsDone) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       [" + task.getStatusIcon() + "] " + task.getDescription());
    }

    /** Prints the chatbot banner and greeting. */
    public void printGreeting() {
        System.out.println("    " + SEPARATOR);
        System.out.println(BANNER);
        System.out.println("     Hello! I'm MyBff.");
        System.out.println("     What can I do for you?");
        System.out.println("    " + SEPARATOR);
        System.out.println();
    }

}
