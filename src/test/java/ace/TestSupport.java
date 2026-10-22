package ace;

import ace.exception.AceException;

/**
 * Provides dependency-free assertions for Ace's regression tests.
 */
public final class TestSupport {
    private static int checks;

    private TestSupport() {
    }

    /** Checks that two values are equal. */
    public static void equal(Object expected, Object actual, String description) {
        checks++;
        if (!java.util.Objects.equals(expected, actual)) {
            throw new AssertionError(description + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }

    /** Checks that a condition is true. */
    public static void check(boolean condition, String description) {
        checks++;
        if (!condition) {
            throw new AssertionError(description);
        }
    }

    /** Checks that an operation rejects invalid input with AceException. */
    public static void rejects(CheckedAction action, String description) {
        checks++;
        try {
            action.run();
        } catch (AceException expected) {
            return;
        } catch (Exception other) {
            throw new AssertionError(description + ": wrong exception " + other, other);
        }
        throw new AssertionError(description + ": expected AceException");
    }

    /** Returns the number of assertions performed by this process. */
    public static int count() {
        return checks;
    }

    /** Operation that can throw a checked exception. */
    @FunctionalInterface
    public interface CheckedAction {
        void run() throws Exception;
    }
}
