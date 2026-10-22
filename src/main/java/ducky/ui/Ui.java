package ducky.ui;

import java.util.Scanner;

import ducky.chore.ChoreList;

/**
 * Handles all interaction with the user: printing messages to the console and
 * reading command lines from standard input. Keeping this in one place means
 * the rest of Ducky does not need to know how input and output actually
 * happen, only what should be shown or read.
 */
public class Ui {
    /** Reads command lines typed by the user on standard input. */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads one line of input typed by the user.
     * If the input has ended (e.g. the user pressed Ctrl+D, or piped-in input
     * ran out), "bai" is returned so that Ducky exits normally instead of crashing.
     *
     * @return The line the user typed, without the trailing newline.
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return "bai";
        }
        return scanner.nextLine();
    }

    /** Prints Ducky's greeting and name banner. */
    public void showWelcome() {
        String banner = " ____             _          \n"
                + "|  _ \\ _   _  ___| | ___   _ \n"
                + "| | | | | | |/ __| |/ / | | |\n"
                + "| |_| | |_| | (__|   <| |_| |\n"
                + "|____/ \\__,_|\\___|_|\\_\\\\__, |\n"
                + "                        |___/ \n";
        System.out.println("QUACK QUACK!!!! I'M");
        System.out.println(banner);
    }

    /** Prints Ducky's farewell message. */
    public void showGoodbye() {
        System.out.println("OK BAI. OFF TO BUY SOME LEMONADE"); // reference to the duck song
    }

    /**
     * Prints a single line of text to the user. Used for confirmations, error
     * messages and any other one-line feedback.
     *
     * @param message The line to print.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints every chore in the given list with its type icon, status icon
     * and rank.
     *
     * @param chores The chore list to display.
     */
    public void showChores(ChoreList chores) {
        for (int i = 0; i < chores.size(); i++) {
            System.out.println(chores.get(i).toNumberedString(i + 1));
        }
    }

    /**
     * Releases the input resource used to read commands. Call this once, when
     * Ducky is about to exit.
     */
    public void close() {
        scanner.close();
    }
}
