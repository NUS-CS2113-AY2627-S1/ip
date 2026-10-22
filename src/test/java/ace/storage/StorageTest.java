package ace.storage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ace.TestSupport;
import ace.task.Deadline;
import ace.task.Event;
import ace.task.Task;
import ace.task.TaskManager;
import ace.task.Todo;

/**
 * Tests persistence in a separate process and temporary working directory.
 * This ensures the real data/ace.txt file is never modified by tests.
 */
public final class StorageTest {
    private StorageTest() {
    }

    public static void run() throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("ace-storage-test-");
        try {
            String javaExecutable = Path.of(System.getProperty("java.home"), "bin", "java").toString();
            String[] classpathEntries = System.getProperty("java.class.path").split(
                    java.util.regex.Pattern.quote(File.pathSeparator));
            for (int i = 0; i < classpathEntries.length; i++) {
                classpathEntries[i] = Path.of(classpathEntries[i]).toAbsolutePath().toString();
            }
            String classpath = String.join(File.pathSeparator, classpathEntries);
            Process process = new ProcessBuilder(javaExecutable, "-cp", classpath,
                    StorageTest.class.getName(), "worker")
                    .directory(temporaryDirectory.toFile())
                    .redirectErrorStream(true)
                    .start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.waitFor();
            TestSupport.equal(0, exitCode, "isolated StorageTest process: " + output);
            System.out.print(output);
        } finally {
            try (var paths = Files.walk(temporaryDirectory)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.deleteIfExists(path);
                }
            }
        }
    }

    private static void runInTemporaryDirectory() throws Exception {
        Storage storage = new Storage();
        TestSupport.equal(0, storage.loadTasks().size(), "missing data file creates empty list");
        TestSupport.check(Files.exists(Path.of("data", "ace.txt")), "storage creates data file");

        TaskManager original = new TaskManager();
        original.addTask(new Todo("compare Python | Java"));
        original.addTask(new Deadline("submit | report", LocalDate.of(2026, 10, 15)));
        original.addTask(new Event("conference | workshop",
                LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 22)));
        original.markAsDone(1);
        storage.saveTasks(original);

        ArrayList<Task> restored = storage.loadTasks();
        TestSupport.equal(3, restored.size(), "all task types reload");
        TestSupport.equal("compare Python | Java", restored.get(0).getLabel(),
                "todo preserves separator");
        TestSupport.equal("submit | report", restored.get(1).getLabel(),
                "deadline preserves separator");
        TestSupport.equal(LocalDate.of(2026, 10, 15),
                ((Deadline) restored.get(1)).getBy(), "deadline preserves date");
        TestSupport.check(restored.get(1).isDone(), "completion status persists");
        TestSupport.equal("conference | workshop", restored.get(2).getLabel(),
                "event preserves separator");
        TestSupport.equal(LocalDate.of(2026, 10, 20),
                ((Event) restored.get(2)).getFrom(), "event preserves start");
        TestSupport.equal(LocalDate.of(2026, 10, 22),
                ((Event) restored.get(2)).getTo(), "event preserves end");

        Path file = Path.of("data", "ace.txt");
        Files.write(file, List.of("T | 0 | old format",
                "D | 1 | old deadline | 2026-10-15",
                "E | 0 | old event | 2026-10-20 | 2026-10-22"));
        ArrayList<Task> legacy = storage.loadTasks();
        TestSupport.equal(3, legacy.size(), "legacy file remains readable");
        TestSupport.equal("old format", legacy.get(0).getLabel(), "legacy todo description");
        TestSupport.equal(LocalDate.of(2026, 10, 15),
                ((Deadline) legacy.get(1)).getBy(), "legacy deadline date");

        Files.writeString(file, "T2 | 0 | !!!not-base64!!!\n");
        TestSupport.rejects(storage::loadTasks, "reject corrupted encoded description");
        Files.writeString(file, "D2 | 0 | c3VibWl0 | 2026-02-30\n");
        TestSupport.rejects(storage::loadTasks, "reject invalid stored date");
        Files.writeString(file, "E2 | 0 | dGVzdA== | 2026-10-22 | 2026-10-20\n");
        TestSupport.rejects(storage::loadTasks, "reject reversed stored event dates");
        System.out.println("PASS StorageTest (" + TestSupport.count() + " isolated assertions)");
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1 || !args[0].equals("worker")) {
            throw new IllegalArgumentException("Run StorageTest through ace.AllTests.");
        }
        runInTemporaryDirectory();
    }
}
