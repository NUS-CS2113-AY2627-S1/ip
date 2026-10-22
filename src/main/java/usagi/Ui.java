package usagi;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Handles all interaction with the user: reading commands typed in, and
 * printing Usagi's responses.
 *
 * Keeping every read and print in one class means the rest of the program
 * never touches {@code System.in} or {@code System.out} directly, so the look
 * of Usagi's responses can be changed in one place.
 */
public class Ui {
    /**
     * Rabbit shown when Usagi starts, since "usagi" means rabbit in Japanese.
     *
     * Drawn with Unicode Braille characters (U+2800 onwards) rather than plain
     * ASCII, so it only displays correctly on a console set to UTF-8.
     */
    private static final String BANNER = """
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠟⠙⣿⠀⢀⣴⠟⠋⢻⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣾⠟⠀⠀⢰⡟⣠⡾⠃⠀⠀⣼⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣾⠃⠀⠀⠀⣿⢣⡟⠁⠀⠀⢰⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⡏⠀⠀⠀⣸⠇⣾⠀⠀⠀⠀⣿⣹⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⠀⠀⠀⢠⡿⣸⡇⠀⠀⠀⣸⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⠀⠀⠀⣾⡇⣿⠀⠀⠀⢰⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⠀⠀⠀⣿⠀⣿⠀⠀⠀⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⣿⡆⠀⠀⣿⠉⣿⠀⠀⠀⡏⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣠⡹⠷⠀⠀⠻⠟⠛⠀⠀⠀⠛⠿⠷⠶⢶⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣼⣾⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⠿⣶⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⡿⠟⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣤⠤⢤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠻⣶⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⣴⡿⠋⠀⠀⠀⡠⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣷⣄⠀⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⢰⣾⠟⠀⠀⠀⢀⡞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢻⣄⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⢀⣾⠋⠀⠀⠀⠀⡼⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠩⢷⡄⠀⠀⠀
            ⠀⠀⠀⠀⣾⡏⠀⠀⠀⠀⠀⠀⠀⢀⣴⠞⠷⣆⠀⠀⠀⠀⠀⠀⢠⡾⠛⢻⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⡇⠀⠀⠀
            ⠀⠀⠀⠀⣿⠁⠀⠀⠀⠀⠀⠀⠀⠸⣿⠶⢶⣿⠀⠀⠀⠀⠀⠀⠸⣿⣶⣺⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣳⠀⠀⠀
            ⠀⠀⠀⠀⣿⠀⠀⠀⠀⠔⢒⡆⢀⠂⢌⠉⠉⠁⢀⠀⢰⣆⠀⢠⡀⠈⠉⠉⠀⣤⠀⣼⢩⡏⢵⡆⠀⠀⠀⠀⠀⠀⠀⡘⣿⠀⠀⠀⠀⠀
            ⠀⠀⠀⠸⣿⡀⠀⠀⠸⠻⡿⠁⠏⠸⢯⠇⠀⠀⠘⠳⣾⣿⣶⡞⠃⠀⠀⠀⠈⡇⠸⠋⠸⢃⢛⠀⠀⠀⠀⠀⠀⠀⠰⣾⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⢻⣧⠀⠀⠀⠀⠐⠒⠒⠂⠁⠀⠀⠀⠀⠀⢻⡿⡟⣷⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⡞⣾⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠈⢿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣷⣆⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⡟⠃⠀⠀⠀
            ⠀⠀⠀⠀⠀⠈⢿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣶⡉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡿⠁⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠙⢿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡿⠁⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡆⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠲⣴⣟⣫⡿⠞⠛⠋⣛⣷⡦⠤⠶⠀⠀⠀⠀⠀⠀⢸⣷⠀⠀⠀⠀⠀⠀
            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣧⠀⠀⠀⠀⠀⠀⠀⣦⣀⣄⡀⠈⠉⠁⣀⣠⣴⠟⠋⠀⠀⣀⣠⡤⠀⠀⠀⠀⠀⢸⣿⠀⠀⠀⠀⠀⠀
            """;

    private static final String HORIZONTAL_LINE = "_".repeat(60);

    /** Reads the lines the user types. */
    private final Scanner scanner;

    /**
     * Creates a Ui that reads from standard input and prints to standard
     * output as UTF-8.
     */
    public Ui() {
        useUtf8Output();
        scanner = new Scanner(System.in);
    }

    /**
     * Switches printed output to UTF-8, so that the banner is not mangled.
     *
     * Java encodes printed text using the platform's default character set,
     * which on Windows is usually Cp1252. That character set has no room for
     * the Braille characters the banner is drawn with, so every one of them
     * would be printed as "?" instead.
     */
    private void useUtf8Output() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }

    /**
     * Reads the next command typed by the user.
     *
     * @return The line typed, with leading and trailing spaces removed.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints the welcome message shown when Usagi starts.
     */
    public void showWelcome() {
        showResponse(BANNER, "Una->(Hello! I'm Usagi.)", "Yaha->(What can I do for you?)");
    }

    /**
     * Prints the message shown just before Usagi exits.
     */
    public void showFarewell() {
        showResponse("U unana una->(Bye. Hope to see you again soon!)");
    }

    /**
     * Prints an error message in the same framed format as other responses.
     *
     * @param message Explanation of what went wrong.
     */
    public void showError(String message) {
        showResponse(message);
    }

    /**
     * Prints the given lines framed by horizontal lines, which is the format
     * Usagi uses for every response.
     *
     * @param lines Lines of the response, printed one per line.
     */
    public void showResponse(String... lines) {
        System.out.println(HORIZONTAL_LINE);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Releases the input stream once Usagi no longer needs to read commands.
     */
    public void close() {
        scanner.close();
    }
}
