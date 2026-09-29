package nia.ui;

/**
 * Handles all of Nia's generic terminal output: prompts, bars, and indented text.
 * Has no knowledge of Nia's personality - that lives in VoicelineManager, which only
 * supplies voiceline strings for callers to print through this class.
 */
public class Printer {
    /** Prints the input prompt without a trailing newline. */
    public static void printPrompt() {
        System.out.print("❯ ");
    }

    /**
     * Prints `message` indented, one indent per line so multiline strings
     * (e.g. the task list) stay aligned under the bar.
     */
    public static void printIndent(String message) {
        final int INDENT_LEVEL = 4;
        String indentedMessage = " ".repeat(INDENT_LEVEL) + message;
        indentedMessage = indentedMessage.replace("\n", "\n" + " ".repeat(INDENT_LEVEL));

        System.out.println(indentedMessage);
    }

    public static void printIndentedError(String message) {
        final int INDENT_LEVEL = 4;
        String indentedMessage = " ".repeat(INDENT_LEVEL) + message;
        indentedMessage = indentedMessage.replace("\n", "\n" + " ".repeat(INDENT_LEVEL));

        System.err.println(indentedMessage);
    }

    /**
     * Prints a horizontal bar spanning the terminal's width.
     * Falls back to a fixed width if the terminal width can't be determined
     * (e.g. when running inside an IDE or a non-interactive shell), since
     * the COLUMNS environment variable is only reliably set by interactive terminals.
     */
    public static void printBar() {

        final String BAR_CHARACTER = "─";
        System.out.println(BAR_CHARACTER.repeat(getTerminalWidth()));
    }

    private static int getTerminalWidth() {
        final int FALLBACK_BAR_LENGTH = 100;
        String columns = System.getenv("COLUMNS");
        if (columns == null) {
            return FALLBACK_BAR_LENGTH;
        }
        try {
            return Integer.parseInt(columns.trim());
        } catch (NumberFormatException e) {
            return FALLBACK_BAR_LENGTH;
        }
    }
}