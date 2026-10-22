package nga;

import java.util.Scanner;

/** Reads console input and displays responses from the command handler. */
public class NgaUi {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String LOGO = "    _ __   __ _  __ _\n"
            + "   | '_ \\ / _` |/ _` |\n"
            + "   | | | | (_| | (_| |\n"
            + "   |_| |_|\\__, |\\__,_|\n"
            + "          |___/";
    private final NgaCommandHandler commandHandler;
    private final Scanner scanner;

    /**
     * Creates a UI connected to standard input.
     *
     * @param commandHandler the handler that processes user commands
     */
    public NgaUi(NgaCommandHandler commandHandler) {
        this(commandHandler, new Scanner(System.in));
    }

    /**
     * Creates a UI with an injectable scanner for tests.
     *
     * @param commandHandler the handler that processes user commands
     * @param scanner the source of user input
     */
    public NgaUi(NgaCommandHandler commandHandler, Scanner scanner) {
        this.commandHandler = commandHandler;
        this.scanner = scanner;
    }

    /** Runs the console interaction until the user exits or input ends. */
    public void run() {
        System.out.println(LOGO + "\n" + DIVIDER
                + "\nHello! I'm Nga (No Goobers Allowed).\n" + DIVIDER);
        while (scanner.hasNextLine()) {
            System.out.println(DIVIDER);
            try {
                CommandResult result = commandHandler.handle(scanner.nextLine());
                System.out.println(result.message());
                if (result.shouldExit()) {
                    return;
                }
            } catch (TaskParseException exception) {
                System.out.println(exception.getMessage());
            } catch (RuntimeException exception) {
                System.out.println(" Sorry, Nga could not complete that command.");
            }
            System.out.println(DIVIDER);
        }
    }
}
