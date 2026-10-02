package foodielover;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Test helper that replaces System.in and System.out for the duration of a test.
 * Use it in a try-with-resources block so the original streams are always restored.
 * Create it BEFORE constructing a Ui, because Ui binds its Scanner to System.in at construction.
 */
public final class ConsoleCapture implements AutoCloseable {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    /**
     * Starts capturing and feeds the given text to System.in.
     *
     * @param input Text that the code under test will read from the keyboard.
     */
    public ConsoleCapture(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    /**
     * Returns everything printed so far, with line endings normalised to \n.
     */
    public String output() {
        return buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Override
    public void close() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
}
