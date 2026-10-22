package bob;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Displays messages and reads commands for Bob's text interface.
 */
public class Ui {
    private final Scanner scanner;
    private static final String INTRO = "========================================\n"
            + "           BOB'S TASK OUTPOST\n"
            + "========================================\n";
    private static final String ASK = "Add 'em, track 'em, get 'em done.\n\n"
            + "Howdy, partner! What's the plan?";
    private static final String BYE = " ____________________________\n"
            + "|                            |\n"
            + "|  Until next time, partner! |\n"
            + "|____________________________|\n";

    /**
     * Creates a text interface that reads standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns the next command, or {@code bye} when the input stream ends.
     *
     * @return Command entered by the user.
     */
    public String readCommand() {
        String userCmd = scanner.hasNextLine() ? scanner.nextLine() : "bye";
        return userCmd;
    }

    /**
     * Displays Bob's introduction and command prompt.
     */
    public void showIntro() {
        System.out.println(INTRO);
        System.out.println(ASK);
    }

    /**
     * Displays Bob's farewell.
     */
    public void showBye() {
        System.out.println(BYE);
    }

    /**
     * Displays every task in the list.
     *
     * @param tasks Tasks to display.
     */
    public void showTasklist(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your saddlebag is empty, partner.");
            return;
        }

        int count = 0;
        for (Task task : tasks) {
            System.out.println((count + 1) + ". " + task);
            count++;
        }
    }

    /**
     * Displays tasks whose descriptions contain the given keyword.
     *
     * @param tasks Tasks to search.
     * @param keyword Text to find in each task description.
     */
    public void showMatchingTasks(ArrayList<Task> tasks, String keyword) {
        int count = 1;
        System.out.println("Here's what I found on the trail:");
        for (Task task : tasks) {
            if (task.description.contains(keyword)) {
                System.out.println(count + ". " + task);
                count++;
            }
        }
        if (count == 1) {
            System.out.println("Nothing turned up on the trail, partner.");
        }
    }

    /**
     * Displays an error message.
     *
     * @param message Explanation of the error.
     */
    public void showError(String message) {
        System.out.println("Hold your horses! " + message);
        System.out.println("----------------------------------------------");
    }

    /**
     * Closes the command input stream.
     */
    public void closeScanner() {
        scanner.close();
    }
}
