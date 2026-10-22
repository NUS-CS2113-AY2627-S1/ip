package ace;

import ace.parser.ParserTest;
import ace.storage.StorageTest;
import ace.task.TaskManagerTest;

/** Entry point for dependency-free regression tests. */
public final class AllTests {
    private AllTests() {
    }

    public static void main(String[] args) throws Exception {
        ParserTest.run();
        TaskManagerTest.run();
        StorageTest.run();
        System.out.println("ALL TESTS PASSED (" + TestSupport.count()
                + " assertions in main process, plus isolated storage assertions)");
    }
}
