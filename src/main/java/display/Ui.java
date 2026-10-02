package display;

import java.util.Scanner;
import list.TaskList;
import task.Task;
import task.Todo;
import task.Deadline;
import task.Event;

    public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;
    private static final String BANNER = "  ___  _____ _        _    ____  \n"
        + " / _ \\|_   _| |      / \\  / ___| \n"
        + "| |_| | | | | |     / _ \\ \\___ \\ \n"
        + "|  _  | | | | |___ / ___ \\ ___) |\n"
        + "|_| |_| |_| |_____/_/   \\_\\____/ \n";

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void displayWelcomeBanner() {
        System.out.println(BANNER);
        System.out.println("Hello! I'm Atlas.");
        System.out.println("What can I do for you?");
    }

    public void displayLine() {
        System.out.println(DIVIDER);
    }

    public String readInput() {
        return scanner.nextLine().trim();
    }

    public void displayEcho(String string) {
        displayLine();
        System.out.println(string);
        displayLine();
    }

    public void displayGoodbye() {
        displayLine();
        System.out.println("Bye. Happy to be of service!");
        displayLine();
    }

    public void displayAdd(Task task, TaskList list) {
        displayLine();
        System.out.println("Added task:");
        System.out.println(task);
        String sizeReturn = list.size() > 1 ? "There are " + list.size() + " items in the list" : "There is " + list.size() + " item in the list";
        System.out.println(sizeReturn);
        displayLine();
    }

    public void displayList(TaskList list) {
        displayLine();
        for (int i = 0; i < list.size(); i++) {
            System.out.print((i + 1) + ". ");
            System.out.println(list.getItem(i));
        }
        displayLine();
    }

    public void displayTaskMarked(Task task) {
        displayLine();
        System.out.println("Marked task as DONE");
        System.out.println(task);
        displayLine();
    }

    public void displayTaskUnmarked(Task task) {
        displayLine();
        System.out.println("Marked task as NOT DONE");
        System.out.println(task);
        displayLine();
    }

    public void displayTaskDeleted(Task task, TaskList list) {
        displayLine();
        System.out.println("The tasks has been removed.");
        System.out.println("   " + task);
        System.out.println("There are " + list.size() + " tasks remaining in the list.");
        displayLine();
    }

    public void displayMatchingTasks(java.util.List<task.Task> matchingTasks) {
        displayLine();
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + ". " + matchingTasks.get(i));
            }
        }
        displayLine();
    }

    public void close() {
        scanner.close();
    }
}
