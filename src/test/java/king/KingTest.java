package king;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Exercises King's command loop using scripted conversations without external libraries.
 */
public class KingTest {

    /**
     * Runs regression tests and fails with an AssertionError if a check fails.
     *
     * @param args Command-line arguments; not used.
     */
    public static void main(String[] args) {
        commands_invalidInput_recoversWithoutAddingTasks();
        deadline_invalidFields_rejectsTask();
        deadline_dates_validatesAndFormats();
        event_invalidFields_rejectsTask();
        taskStatus_invalidIndex_preservesState();
        commands_validInput_preservesLevelFourBehavior();
        taskList_overOneHundred_growsDynamically();
        delete_validIndices_removesAndRenumbersTasks();
        delete_invalidIndices_preservesTasks();
        input_endOfStream_exitsCleanly();
        bye_validCommand_stopsReading();
        find_matchingDescriptions_returnsOnlyMatches();
        find_noMatches_reportsEmptyResults();
        find_missingKeyword_recovers();
        find_phrase_preservesListOrderAndState();
        System.out.println("All 15 King regression tests passed.");
    }

    /**
     * Verifies recovery from invalid commands without adding unwanted tasks.
     */
    private static void commands_invalidInput_recoversWithoutAddingTasks() {
        String output = runConversation("todo\n   \nblah\nlist extra\nbye extra\ntodo read a book\nlist\nbye\n");
        assertOccurrences(output, "My subject,", 5);
        assertContains(output, "a todo needs a description");
        assertContains(output, "please enter a command");
        assertContains(output, "I do not recognize 'blah'");
        assertContains(output, "'list' takes no arguments");
        assertContains(output, "'bye' takes no arguments");
        assertContains(output, "1. [T][ ] read a book");
        assertOccurrences(output, "I've added this task", 1);
        assertContains(output, "Bye. Hope to see you again soon!");
    }

    /**
     * Verifies rejection of missing or repeated deadline fields.
     */
    private static void deadline_invalidFields_rejectsTask() {
        String[] commands = {
            "deadline", "deadline return book", "deadline /by 2019-10-15",
            "deadline return book /by", "deadline return book /by 2019-10-15 /by Saturday"
        };
        String output = runConversation(String.join("\n", commands) + "\ntodo safe\nlist\nbye\n");
        assertOccurrences(output, "My subject,", commands.length);
        assertContains(output, "a deadline needs a description");
        assertContains(output, "a deadline needs a date after /by");
        assertOccurrences(output, "I've added this task", 1);
        assertContains(output, "1. [T][ ] safe");
    }

    /**
     * Verifies invalid dates, leap days, and formatted deadline output.
     */
    private static void deadline_dates_validatesAndFormats() {
        String[] invalidDates = {"Friday", "2019-02-29", "2024-02-30", "2024-13-01", "2024-00-01",
            "2024-04-31", "15/10/2019", "2019-1-5", "2019-10-15 1800"};
        StringBuilder commands = new StringBuilder();
        for (String date : invalidDates) {
            commands.append("deadline invalid /by ").append(date).append('\n');
        }
        commands.append("deadline leap day /by 2024-02-29\nlist\nbye\n");
        String output = runConversation(commands.toString());
        assertOccurrences(output, "a deadline needs a valid date in yyyy-MM-dd format", invalidDates.length);
        assertOccurrences(output, "I've added this task", 1);
        assertContains(output, "1. [D][ ] leap day (by: Feb 29 2024)");
    }

    /**
     * Verifies rejection of missing, repeated, or misplaced event clauses.
     */
    private static void event_invalidFields_rejectsTask() {
        String[] commands = {
            "event", "event meeting", "event meeting /from Monday",
            "event /from Monday /to Tuesday", "event meeting /from /to Tuesday",
            "event meeting /from Monday /to", "event meeting /to Tuesday /from Monday",
            "event meeting /from Monday /from Tuesday /to Wednesday",
            "event meeting /from Monday /to Tuesday /to Wednesday",
            "event meeting /to Sunday /from Monday /to Tuesday"
        };
        String output = runConversation(String.join("\n", commands) + "\ntodo safe\nlist\nbye\n");
        assertOccurrences(output, "My subject,", commands.length);
        assertContains(output, "an event needs a description");
        assertContains(output, "an event needs a start time after /from");
        assertContains(output, "an event needs an end time after /to");
        assertOccurrences(output, "I've added this task", 1);
        assertContains(output, "1. [T][ ] safe");
    }

    /**
     * Verifies that invalid task numbers leave completion states unchanged.
     */
    private static void taskStatus_invalidIndex_preservesState() {
        String[] invalidIndices = {"", "abc", "1.5", "1 2", "999999999999999999999", "0", "-1", "2"};
        StringBuilder commands = new StringBuilder("mark 1\nunmark 1\ntodo safe\nmark 1\n");
        for (String index : invalidIndices) {
            commands.append("unmark ").append(index).append('\n');
        }
        commands.append("list\nunmark 1\n");
        for (String index : invalidIndices) {
            commands.append("mark ").append(index).append('\n');
        }
        commands.append("list\nbye\n");
        String output = runConversation(commands.toString());
        assertOccurrences(output, "My subject,", 2 + 2 * invalidIndices.length);
        assertOccurrences(output, "your list is empty", 2);
        assertContains(output, "1. [T][X] safe");
        assertContains(output, "1. [T][ ] safe");
    }

    /**
     * Verifies creation, listing, and completion changes with mixed-case commands.
     */
    private static void commands_validInput_preservesLevelFourBehavior() {
        String output = runConversation("  TODO\tread a book  \n"
                + "DeAdLiNe\treturn book\t/by\t2019-10-15\n"
                + "EVENT meeting /from Monday 2pm /to Monday 4pm\n"
                + "todo visit /bypass\nmark\t2\nlist\nunmark 2\nlist\nBYE\n");
        assertOccurrences(output, "My subject,", 0);
        assertContains(output, "1. [T][ ] read a book");
        assertContains(output, "2. [D][X] return book (by: Oct 15 2019)");
        assertContains(output, "2. [D][ ] return book (by: Oct 15 2019)");
        assertContains(output, "3. [E][ ] meeting (from: Monday 2pm to: Monday 4pm)");
        assertContains(output, "4. [T][ ] visit /bypass");
        assertOccurrences(output, "I've added this task", 4);
    }

    /**
     * Verifies that the task list grows beyond one hundred entries.
     */
    private static void taskList_overOneHundred_growsDynamically() {
        StringBuilder commands = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            commands.append("todo task ").append(i).append('\n');
        }
        commands.append("todo overflow\ndeadline overflow /by 2019-10-15\n"
                + "event overflow /from Monday /to Tuesday\nblah\nmark 100\nlist\nbye\n");
        String output = runConversation(commands.toString());
        assertOccurrences(output, "I've added this task", 103);
        assertOccurrences(output, "My subject,", 1);
        assertContains(output, "100. [T][X] task 100");
        assertContains(output, "103. [E][ ] overflow (from: Monday to: Tuesday)");
        assertContains(output, "Bye. Hope to see you again soon!");
    }

    /**
     * Verifies removal, renumbering, and reuse of an emptied task list.
     */
    private static void delete_validIndices_removesAndRenumbersTasks() {
        String output = runConversation("todo first\ndeadline middle /by 2019-10-15\n"
                + "event last /from noon /to night\nmark 2\ndelete 2\nlist\n"
                + "delete 2\nDELETE\t1\nlist\ntodo new\nlist\nbye\n");
        assertOccurrences(output, "My subject,", 0);
        assertOccurrences(output, "I've removed this task", 3);
        assertContains(output, "[D][X] middle (by: Oct 15 2019)");
        assertContains(output, "2. [E][ ] last (from: noon to: night)");
        assertContains(output, "Now you have 0 task(s)");
        assertContains(output, "1. [T][ ] new");
    }

    /**
     * Verifies that invalid delete arguments leave tasks intact.
     */
    private static void delete_invalidIndices_preservesTasks() {
        String output = runConversation("delete 1\ntodo safe\ndelete\ndelete abc\ndelete 1.5\n"
                + "delete 0\ndelete -1\ndelete 2\ndelete 999999999999999999\ndelete 1 2\nlist\nbye\n");
        assertOccurrences(output, "My subject,", 9);
        assertOccurrences(output, "I've removed this task", 0);
        assertContains(output, "1. [T][ ] safe");
    }

    /**
     * Verifies that exhausted input ends the session after the final command.
     */
    private static void input_endOfStream_exitsCleanly() {
        assertContains(runConversation(""), "Hello, my subject.");
        assertContains(runConversation("todo unfinished input"), "Now you have 1 task(s)");
        assertContains(runConversation("blah"), "I do not recognize 'blah'");
    }

    /**
     * Verifies that commands following a valid exit command are ignored.
     */
    private static void bye_validCommand_stopsReading() {
        String output = runConversation("bye\ntodo ignored\n");
        assertContains(output, "Bye. Hope to see you again soon!");
        assertOccurrences(output, "I've added this task", 0);
    }

    /**
     * Verifies description matching and numbering across all task types.
     */
    private static void find_matchingDescriptions_returnsOnlyMatches() {
        String output = runConversation("todo unrelated\ntodo read book\n"
                + "deadline return book /by 2019-10-15\nevent book club /from noon /to night\n"
                + "todo notebook\ntodo Book\nmark 2\n  FIND\tbook  \nbye\n");
        String results = output.substring(output.indexOf("Here are the matching tasks in your list:"));
        assertContains(results, "1. [T][X] read book");
        assertContains(results, "2. [D][ ] return book (by: Oct 15 2019)");
        assertContains(results, "3. [E][ ] book club (from: noon to: night)");
        assertContains(results, "4. [T][ ] notebook");
        assertOccurrences(results, "unrelated", 0);
        assertOccurrences(results, "Book", 0);
        assertOccurrences(results, "5. [", 0);
    }

    /**
     * Verifies empty results and exclusion of displayed dates and event times.
     */
    private static void find_noMatches_reportsEmptyResults() {
        assertContains(runConversation("find book\nbye\n"), "No matching tasks found.");
        String output = runConversation("deadline return /by 2019-10-15\n"
                + "event meeting /from noon /to night\nfind Oct\nfind noon\nfind absent\nbye\n");
        assertOccurrences(output, "No matching tasks found.", 3);
        assertOccurrences(output, "My subject,", 0);
    }

    /**
     * Verifies recovery after search commands without keywords.
     */
    private static void find_missingKeyword_recovers() {
        String output = runConversation("find\nfind   \ntodo book\nfind book\nbye\n");
        assertOccurrences(output, "please supply a search keyword. Try: find book", 2);
        assertContains(output, "1. [T][ ] book");
    }

    /**
     * Verifies phrase matching without changing task order or completion.
     */
    private static void find_phrase_preservesListOrderAndState() {
        String output = runConversation("todo first\ntodo read book\ntodo book read\n"
                + "mark 2\nfind read book\nlist\nbye\n");
        String results = output.substring(output.indexOf("Here are the matching tasks in your list:"),
                output.indexOf("Here are the tasks in your list:"));
        assertContains(results, "1. [T][X] read book");
        assertOccurrences(results, "book read", 0);
        assertOccurrences(results, "first", 0);
        assertContains(output, "1. [T][ ] first");
        assertContains(output, "2. [T][X] read book");
        assertContains(output, "3. [T][ ] book read");
    }

    /**
     * Runs a fresh chatbot session and restores the process streams afterward.
     *
     * @param input Commands separated by newline characters.
     * @return Captured output from the session.
     */
    private static String runConversation(String input) {
        try {
            Path directory = Files.createTempDirectory("king-command-test-");
            Path file = directory.resolve("king.txt");
            try {
                return runConversation(input, file);
            } finally {
                Files.deleteIfExists(file);
                Files.delete(directory);
            }
        } catch (IOException exception) {
            throw new AssertionError("Could not prepare isolated test storage", exception);
        }
    }

    /**
     * Runs a session against a specific save file for persistence tests.
     *
     * @param input Commands separated by newline characters.
     * @param file Isolated save path used by the test or conversation.
     * @return Captured output from the session.
     */
    static String runConversation(String input, Path file) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capturedOutput);
            King.run(file);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    /**
     * Checks that captured output contains expected text, reporting the output on failure.
     *
     * @param output Captured console output.
     * @param expected Expected text.
     */
    private static void assertContains(String output, String expected) {
        if (!output.contains(expected)) {
            throw new AssertionError("Missing output: " + expected + "\nActual output:\n" + output);
        }
    }

    /**
     * Checks the number of non-overlapping occurrences of expected text in captured output.
     *
     * @param output Captured console output.
     * @param expected Expected text.
     * @param expectedCount Required number of occurrences.
     */
    private static void assertOccurrences(String output, String expected, int expectedCount) {
        int count = 0;
        int offset = 0;
        while ((offset = output.indexOf(expected, offset)) >= 0) {
            count++;
            offset += expected.length();
        }
        if (count != expectedCount) {
            throw new AssertionError("Expected " + expectedCount + " occurrences of '" + expected
                    + "' but found " + count + "\nActual output:\n" + output);
        }
    }
}
