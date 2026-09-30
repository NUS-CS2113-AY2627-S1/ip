package twitchchat;

import twitchchat.commands.Command;
import twitchchat.commands.CommandType;
import twitchchat.exceptions.TwitchChatCommandException;
import twitchchat.exceptions.TwitchChatInvalidCommandException;
import twitchchat.exceptions.TwitchChatInvalidTaskIdException;
import twitchchat.exceptions.TwitchChatMissingArgumentException;
import twitchchat.exceptions.TwitchChatTooManyArgumentsException;
import twitchchat.tasks.TaskList;

/**
 * Runs a smoke test for the basic TwitchChat commands.
 *
 * Run the class directly from IntelliJ to exercise
 * the parser and command handler without entering each command manually.
 */
public class BasicCommandsTest {

    private final Parser parser = new Parser();
    private final TaskList tasks = new TaskList();
    private final CommandHandler commandHandler = new CommandHandler(tasks, new Ui());

    /**
     * Runs the basic command flow and parser validation checks.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        BasicCommandsTest test = new BasicCommandsTest();
        test.runsBasicCommandFlow();
        test.rejectsInvalidDeleteInput();
        test.runsFindCommand();
        test.rejectsInvalidFindInput();
        System.out.println("All basic command tests passed.");
    }

    private void runsBasicCommandFlow() {
        run("hi");
        run("todo buy milk");
        run("deadline submit report /by tomorrow");
        run("event team meeting /from Monday /to Tuesday");
        check(tasks.findTasks("REPORT").size() == 1, "Expected a case-insensitive report search match");
        run("find report");
        run("list");
        run("mark 1");
        run("unmark 1");
        run("delete 2");
        check(tasks.getTaskCount() == 2, "Expected two tasks after deletion");
        run("bye");
    }

    private void rejectsInvalidDeleteInput() {
        assertThrows(TwitchChatMissingArgumentException.class, () -> parser.parse("delete"));
        assertThrows(TwitchChatInvalidCommandException.class, () -> parser.parse("delete2"));
        assertThrows(TwitchChatTooManyArgumentsException.class, () -> parser.parse("delete 1 2"));
        assertThrows(TwitchChatInvalidTaskIdException.class, () -> parser.parse("delete one"));
    }

    private void runsFindCommand() {
        Command command = parser.parse("find report");
        check(command.getType() == CommandType.FIND, "Expected the find command type");
        check(command.getArguments().length == 1 && command.getArguments()[0].equals("report"),
                "Expected the find command to contain its search term");
    }

    private void rejectsInvalidFindInput() {
        assertThrows(TwitchChatMissingArgumentException.class, () -> parser.parse("find"));
        assertThrows(TwitchChatInvalidCommandException.class, () -> parser.parse("findreport"));
    }

    private void run(String input) {
        Command command = parser.parse(input);
        commandHandler.executeCommand(command);
    }

    private void assertThrows(Class<? extends TwitchChatCommandException> expectedType,
                              Runnable action) {
        try {
            action.run();
            throw new AssertionError("Expected " + expectedType.getSimpleName());
        } catch (TwitchChatCommandException exception) {
            check(expectedType.isInstance(exception),
                    "Expected " + expectedType.getSimpleName()
                            + " but got " + exception.getClass().getSimpleName());
        }
    }

    private void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
