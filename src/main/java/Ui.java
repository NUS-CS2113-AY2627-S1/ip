import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = " _____             _          \n"
            + "|  ___|   _ _ __  | | ___   _ \n"
            + "| |_ | | | | '_ \\ | |/ / | | |\n"
            + "|  _|| |_| | | | ||   <| |_| |\n"
            + "|_|   \\__,_|_| |_||_|\\_\\\\__, |\n"
            + "                         |___/ \n";

    private final Scanner in = new Scanner(System.in);

    public void showWelcome() {
        System.out.println(BANNER);
        showLine();
        System.out.println("Hello! I'm Funky");
        System.out.println("What can I do for you?");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public String readCommand() {
        return in.hasNextLine() ? in.nextLine() : "bye";
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showLoadWarnings(List<String> warnings) {
        if (warnings.isEmpty()) {
            return;
        }
        for (String warning : warnings) {
            showError(warning);
        }
        showLine();
    }

    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void showMatchingTasks(List<Task> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". " + matches.get(i));
        }
    }

    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }
}