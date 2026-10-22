package wallis;

import java.util.Scanner;

/**
 * Handles all user interactions, including reading inputs and printing messages to the console.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a new Ui instance and initializes the Scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the initial welcome message to the user.
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Wallis");
        System.out.println(" What can I do for you?");
        showLine();
    }

    /**
     * Displays the farewell message before the application closes.
     */
    public void showGoodbye() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Prints a standardized horizontal divider line.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Reads the next command typed by the user.
     *
     * @return The trimmed string input from the user.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }
    
    /**
     * Displays an error message.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Displays a default error message when the save file fails to load.
     */
    public void showLoadingError() {
        System.out.println(" Error loading tasks from file.");
    }
}