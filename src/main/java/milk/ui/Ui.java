package milk.ui;

import java.util.Scanner;

/**
 * Contains the methods used for user interaction.
 */
public class Ui {
    private final String linePrefix = "> ";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next user line.
     * @return Line input by user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints out chatbot's greeting.
     * Used on startup.
     */
    public void printGreeting() {
        String banner = """
                 __  __   _   _   _        _   _
                |  \\/  | (_) | | | | __   | | | |
                | |  | | | | | | |   <    |_| |_|
                |_|  |_| |_| |_| |_|\\_\\   (_) (_)
                """;
        // used https://www.asciiart.eu/text-to-ascii-art for this!
        System.out.println(banner);
        System.out.println(linePrefix + "Milk is here!! What do you need today?");
    }

    /**
     * Prints chatbot's responses.
     * @param response String to be printed.
     */
    public void printResponse(String response) {
        System.out.println(linePrefix + response);
    }

    /**
     * Prints chatbot's responses.
     * If includePrefix is false, the chatbox does not print out a prefix before the response.
     * @param response
     * @param includePrefix
     */
    public void printResponse(String response, boolean includePrefix) {
        if (includePrefix) {
            System.out.println(linePrefix + response);
        } else {
            System.out.println(response);
        }
    }

    /**
     * Print's goodbye message.
     * Used on chatbot exit.
     */
    public void printGoodbye() {
        System.out.println(linePrefix + "See you next time!~");
    }
}
