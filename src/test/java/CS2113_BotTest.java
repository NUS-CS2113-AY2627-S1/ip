import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Checks that changing tasks writes their current state to the data file.
 */
public class CS2113_BotTest {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private static final Path MALFORMED_DATA_FILE = Path.of("data", "malformed-duke.txt");
    private static final Path MISSING_DATA_FILE = Path.of("data", "missing-duke.txt");

    /**
     * Runs the persistence happy-path checks without an external test framework.
     *
     * @param args command-line arguments, which are not used
     * @throws Exception if a check fails or the test setup cannot be completed
     */
    public static void main(String[] args) throws Exception {
        boolean hadExistingData = Files.exists(DATA_FILE);
        String existingData = hadExistingData
                ? Files.readString(DATA_FILE, StandardCharsets.UTF_8) : null;
        try {
            resetTaskList();
            Files.deleteIfExists(DATA_FILE);

            CS2113_Bot.handleTodo("todo read book");
            CS2113_Bot.handleDeadline("deadline return book /by June 6th");
            CS2113_Bot.handleEvent("event project meeting /from Aug 6th 2pm /to Aug 6th 4pm");
            CS2113_Bot.handleMark("mark 1", true);

            String expected = String.join(System.lineSeparator(),
                    "T | 1 | read book",
                    "D | 0 | return book | June 6th",
                    "E | 0 | project meeting | Aug 6th 2pm | Aug 6th 4pm")
                    + System.lineSeparator();
            String actual = Files.readString(DATA_FILE, StandardCharsets.UTF_8);
            if (!expected.equals(actual)) {
                throw new AssertionError("Saved task data did not match the current task list.");
            }

            resetTaskList();
            loadTasks();
            assertRestoredTasks();

            resetTaskList();
            Files.deleteIfExists(MISSING_DATA_FILE);
            loadTasks(MISSING_DATA_FILE);
            assertTaskCount(0);

            loadTasks();
            assertRestoredTasks();

            Files.writeString(MALFORMED_DATA_FILE, "X | 0 | unknown task", StandardCharsets.UTF_8);
            assertLoadFails(MALFORMED_DATA_FILE);
            assertTaskCount(3);

            try {
                CS2113_Bot.handleTodo("todo cannot | be saved");
                throw new AssertionError("An unsafe task description was accepted.");
            } catch (CS2113BotException expectedException) {
                assertTaskCount(3);
            }
        } finally {
            Files.deleteIfExists(MALFORMED_DATA_FILE);
            if (hadExistingData) {
                Files.writeString(DATA_FILE, existingData, StandardCharsets.UTF_8);
            } else {
                Files.deleteIfExists(DATA_FILE);
            }
        }
    }

    /**
     * Clears the chatbot's static task list so this test starts from a known state.
     *
     * @throws ReflectiveOperationException if the fields cannot be accessed
     */
    private static void resetTaskList() throws ReflectiveOperationException {
        Field taskList = CS2113_Bot.class.getDeclaredField("taskList");
        taskList.setAccessible(true);
        taskList.set(null, new Task[100]);

        Field taskCount = CS2113_Bot.class.getDeclaredField("taskCount");
        taskCount.setAccessible(true);
        taskCount.setInt(null, 0);
    }

    /**
     * Invokes the private startup loader to test a save-and-restart round trip.
     *
     * @throws ReflectiveOperationException if the loader cannot be accessed
     */
    private static void loadTasks() throws ReflectiveOperationException {
        loadTasks(DATA_FILE);
    }

    /**
     * Invokes the private startup loader for the supplied data file.
     *
     * @param path path of the data file to load
     * @throws ReflectiveOperationException if the loader cannot be accessed
     */
    private static void loadTasks(Path path) throws ReflectiveOperationException {
        Method readTasks = CS2113_Bot.class.getDeclaredMethod("readTasks", String.class);
        readTasks.setAccessible(true);
        readTasks.invoke(null, path.toString());
    }

    /**
     * Verifies that malformed data is reported without replacing the valid in-memory list.
     *
     * @param path path of a malformed data file
     * @throws ReflectiveOperationException if the loader cannot be accessed
     */
    private static void assertLoadFails(Path path) throws ReflectiveOperationException {
        try {
            loadTasks(path);
            throw new AssertionError("Malformed task data was accepted.");
        } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof CS2113BotException)) {
                throw e;
            }
        }
    }

    /**
     * Checks the number of tasks currently held by the chatbot.
     *
     * @param expectedCount expected task count
     * @throws ReflectiveOperationException if the task count cannot be accessed
     */
    private static void assertTaskCount(int expectedCount) throws ReflectiveOperationException {
        Field taskCount = CS2113_Bot.class.getDeclaredField("taskCount");
        taskCount.setAccessible(true);
        if (taskCount.getInt(null) != expectedCount) {
            throw new AssertionError("Task count changed unexpectedly.");
        }
    }

    /**
     * Verifies that the saved task types, fields, and completion status were restored.
     *
     * @throws ReflectiveOperationException if the task list cannot be accessed
     */
    private static void assertRestoredTasks() throws ReflectiveOperationException {
        Field taskList = CS2113_Bot.class.getDeclaredField("taskList");
        taskList.setAccessible(true);
        Task[] restoredTasks = (Task[]) taskList.get(null);

        if (!(restoredTasks[0] instanceof Todo) || !restoredTasks[0].isDone
                || !restoredTasks[0].description.equals("read book")) {
            throw new AssertionError("Todo was not restored correctly.");
        }
        if (!(restoredTasks[1] instanceof Deadline) || restoredTasks[1].isDone
                || !((Deadline) restoredTasks[1]).by.equals("June 6th")) {
            throw new AssertionError("Deadline was not restored correctly.");
        }
        if (!(restoredTasks[2] instanceof Event) || restoredTasks[2].isDone
                || !((Event) restoredTasks[2]).from.equals("Aug 6th 2pm")
                || !((Event) restoredTasks[2]).to.equals("Aug 6th 4pm")) {
            throw new AssertionError("Event was not restored correctly.");
        }
    }
}
