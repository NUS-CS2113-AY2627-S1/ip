package king;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import king.exception.KingException;
import king.storage.Storage;
import king.task.Deadline;
import king.task.Event;
import king.task.Task;
import king.task.ToDo;

/**
 * Verifies storage and restart behavior using files beneath an isolated temporary directory.
 */
public class KingPersistenceTest {

    /**
     * Runs persistence checks and removes only the temporary files created by this suite.
     *
     * @param args Command-line arguments; not used.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    public static void main(String[] args) throws Exception {
        Path directory = Files.createTempDirectory("king-persistence-test-");
        try {
            storage_allTaskTypes_roundTripWithoutLoss(directory.resolve("round-trip.txt"));
            commands_changes_saveBeforeExit(directory.resolve("commands.txt"));
            commands_noChanges_preserveFile(directory.resolve("unchanged.txt"));
            storage_missingParents_createsOnSave(directory.resolve("nested/data/king.txt"));
            storage_corruptRecords_preservesOriginal(directory.resolve("corrupt.txt"));
            storage_invalidDeadline_preservesOriginal(directory.resolve("invalid-date.txt"));
            storage_readFailure_stopsSession(directory.resolve("not-a-file"));
            storage_saveFailure_reportsAndRetries(directory.resolve("blocked"));
            commands_largeSavedList_supportsDeletionAndStatus(directory.resolve("large.txt"));
            System.out.println("All 9 persistence tests passed.");
        } finally {
            try (Stream<Path> paths = Files.walk(directory)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(path);
                }
            }
        }
    }

    /**
     * Verifies repeated persistence of task fields, dates, and completion states.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void storage_allTaskTypes_roundTripWithoutLoss(Path file) throws Exception {
        Task todo = new ToDo("read | book\\notes\t\n\u4e66");
        Task deadline = new Deadline("return book (by: literal)", "2024-02-29");
        Task event = new Event("meeting", "Monday\t2pm", "Tuesday\n4pm");
        deadline.markAsDone();
        event.markAsDone();
        List<Task> expectedTasks = List.of(todo, deadline, event);
        Storage storage = new Storage(file);
        storage.save(expectedTasks);
        for (int round = 0; round < 3; round++) {
            List<Task> actualTasks = storage.load();
            check(actualTasks.size() == 3, "Task count must survive reload");
            for (int i = 0; i < expectedTasks.size(); i++) {
                Task expected = expectedTasks.get(i);
                Task actual = actualTasks.get(i);
                check(expected.getClass().equals(actual.getClass()), "Task type must survive reload");
                check(expected.getDescription().equals(actual.getDescription()), "All fields must survive reload");
                check(expected.isDone() == actual.isDone(), "Completion state must survive reload");
            }
            check(((Deadline) actualTasks.get(1)).getBy().equals(LocalDate.of(2024, 2, 29)),
                    "Deadline must remain a LocalDate through reload");
            storage.save(actualTasks);
        }
        storage.save(List.of());
        check(storage.load().isEmpty(), "An empty snapshot must replace previous tasks");
    }

    /**
     * Verifies immediate persistence and restoration in a later session.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void commands_changes_saveBeforeExit(Path file) throws Exception {
        KingTest.runConversation("todo read\ndeadline return /by 2019-10-15\nevent meet /from noon /to night\n", file);
        Storage storage = new Storage(file);
        check(storage.load().size() == 3, "Adds must save without bye");
        KingTest.runConversation("mark 2\n", file);
        check(storage.load().get(1).isDone(), "Mark must save before exit");
        String output = KingTest.runConversation("list\nunmark 2\n", file);
        check(output.contains("2. [D][X] return (by: Oct 15 2019)"), "Startup must restore completed tasks");
        check(!storage.load().get(1).isDone(), "Unmark must save before exit");
        KingTest.runConversation("delete 2\n", file);
        List<Task> remainingTasks = storage.load();
        check(remainingTasks.size() == 2, "Delete must save before exit");
        check(remainingTasks.get(1) instanceof Event, "Delete must preserve and renumber the remaining event");
        KingTest.runConversation("delete 1\ndelete 1\n", file);
        check(storage.load().isEmpty(), "Deleting everything must save an empty list");
        check(!KingTest.runConversation("list\nbye\n", file).contains("1. ["), "Deleted tasks must not reappear");
    }

    /**
     * Verifies preservation of file contents and timestamps by read-only and rejected commands.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void commands_noChanges_preserveFile(Path file) throws Exception {
        new Storage(file).save(List.of(new ToDo("safe")));
        byte[] original = Files.readAllBytes(file);
        Files.setLastModifiedTime(file, FileTime.fromMillis(1000000000000L));
        FileTime originalTime = Files.getLastModifiedTime(file);
        KingTest.runConversation("list\nfind safe\nfind absent\nfind\ntodo\nblah\nmark 9\ndelete 9\nbye\n", file);
        check(Arrays.equals(original, Files.readAllBytes(file)), "Rejected commands must preserve saved content");
        check(originalTime.equals(Files.getLastModifiedTime(file)), "Read-only commands must not rewrite the file");
    }

    /**
     * Verifies that missing storage directories are created only when saving.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void storage_missingParents_createsOnSave(Path file) throws Exception {
        check(new Storage(file).load().isEmpty(), "Missing folders must load as an empty list");
        KingTest.runConversation("list\nbye\n", file);
        check(!Files.exists(file.getParent()), "Read-only first run need not create folders");
        KingTest.runConversation("todo first run\n", file);
        check(new Storage(file).load().size() == 1, "First change must create folders and save");
    }

    /**
     * Verifies that malformed records stop loading without overwriting the file.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void storage_corruptRecords_preservesOriginal(Path file) throws Exception {
        String[] invalidRecords = {
            "garbage", "X|0|eA==", "T|2|eA==", "T|0|", "T|0|%%%", "T|0|/w==",
            "T|0|eA==|extra", "D|0|eA==", "D|0|eA==|", "E|0|eA==|eA==", "", "T|0|IA=="
        };
        for (String record : invalidRecords) {
            Files.writeString(file, "T|0|c2FmZQ==\n" + record + "\n", StandardCharsets.UTF_8);
            byte[] original = Files.readAllBytes(file);
            String output = KingTest.runConversation("todo should not overwrite\nbye\n", file);
            check(output.contains("invalid saved data at line 2"), "Corruption must report its line number");
            check(!output.contains("I've added"), "Load failure must stop command processing");
            check(Arrays.equals(original, Files.readAllBytes(file)), "Corrupt data must remain untouched");
        }
        Files.write(file, new byte[] {(byte) 0xff});
        check(KingTest.runConversation("bye\n", file).contains("cannot read"), "Invalid file UTF-8 must be reported");
    }

    /**
     * Verifies rejection of invalid deadline text without changing the save file.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void storage_invalidDeadline_preservesOriginal(Path file) throws Exception {
        for (String date : List.of("Friday", "2019-02-29", "Oct 15 2019")) {
            String encodedDate = Base64.getEncoder().encodeToString(date.getBytes(StandardCharsets.UTF_8));
            Files.writeString(file, "D|0|Ym9vaw==|" + encodedDate + "\n", StandardCharsets.UTF_8);
            byte[] original = Files.readAllBytes(file);
            String output = KingTest.runConversation("todo should not overwrite\nbye\n", file);
            check(output.contains("invalid saved data at line 1"), "Invalid deadline must report a loading error");
            check(Arrays.equals(original, Files.readAllBytes(file)), "Invalid deadline must preserve the save file");
        }
    }

    /**
     * Verifies that an unreadable save path stops the session and preserves data.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void storage_readFailure_stopsSession(Path file) throws Exception {
        Files.createDirectory(file);
        Path sentinel = file.resolve("keep.txt");
        Files.writeString(sentinel, "keep");
        String output = KingTest.runConversation("todo unsafe\n", file);
        check(output.contains("cannot read"), "A directory at the file path must produce a read error");
        check(Files.readString(sentinel).equals("keep"), "Read failure must preserve existing data");
    }

    /**
     * Verifies save failure reporting and retry after repairing the destination.
     *
     * @param blocker Temporary path used to simulate an unwritable destination.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void storage_saveFailure_reportsAndRetries(Path blocker) throws Exception {
        Files.writeString(blocker, "keep");
        Storage storage = new Storage(blocker.resolve("king.txt"));
        try {
            storage.save(List.of(new ToDo("retry")));
            throw new AssertionError("Saving beneath a regular file must fail");
        } catch (KingException exception) {
            check(exception.getMessage().contains("Changes remain in memory only"), "Save failure must be explicit");
        }
        check(Files.readString(blocker).equals("keep"), "Failed save must not overwrite the blocking file");
        Files.delete(blocker);
        storage.save(List.of(new ToDo("retry")));
        check(storage.load().getFirst().getRawDescription().equals("retry"), "Saving must recover after repair");
    }

    /**
     * Verifies loading, deletion, and completion changes with over one hundred tasks.
     *
     * @param file Isolated save path used by the test or conversation.
     * @throws Exception If test setup, storage access, or cleanup fails.
     */
    private static void commands_largeSavedList_supportsDeletionAndStatus(Path file) throws Exception {
        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= 105; i++) {
            tasks.add(new ToDo("task " + i));
        }
        Storage storage = new Storage(file);
        storage.save(tasks);
        String output = KingTest.runConversation("list\ndelete 101\nmark 104\n", file);
        check(output.contains("105. [T][ ] task 105"), "Startup must restore more than 100 tasks");
        List<Task> restoredTasks = storage.load();
        check(restoredTasks.size() == 104, "Deletion must persist in a large list");
        check(restoredTasks.get(100).getRawDescription().equals("task 102"), "Remaining tasks must shift correctly");
        check(restoredTasks.getLast().isDone(), "Status changes must use the renumbered indices");
    }

    /**
     * Fails the test with an explanation when the supplied condition is false.
     *
     * @param isSatisfied Condition required to pass.
     * @param message Explanation to display or include in a failure.
     */
    private static void check(boolean isSatisfied, String message) {
        if (!isSatisfied) {
            throw new AssertionError(message);
        }
    }
}
