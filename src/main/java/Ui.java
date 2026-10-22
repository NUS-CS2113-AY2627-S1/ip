import java.util.Scanner;

/** Handles input and console output for the Extant chatbot. */
public class Ui implements AutoCloseable {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;

    /** Creates a UI backed by standard input and output. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Prints the application's welcome message. */
    public void showWelcome() {
        System.out.println("Good day. State your intent.");
        showLine();
    }

    /** Prints a horizontal divider line. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Reads the next command entered by the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints a message surrounded by divider lines. */
    public void showMessage(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /** Displays all tasks in a task list. */
    public void showTaskList(TaskList tasks) {
        showLine();
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(formatTask(tasks.get(i), i));
        }
        System.out.println("You have " + tasks.size() + " tasks in total.");
        showLine();
    }

    /** Displays tasks matching a search term. */
    public void showMatchingTasks(java.util.ArrayList<Task> matches) {
        showLine();
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println(formatTask(matches.get(i), i));
            }
        }
        showLine();
    }

    /** Formats one task with its number and completion status. */
    private String formatTask(Task task, int index) {
        return (index + 1) + ". [" + task.getStatusIcon() + "]["
                + (task.getDone() ? "X" : " ") + "] " + task;
    }

    /** Closes the input reader. */
    @Override
    public void close() {
        scanner.close();
    }
}
