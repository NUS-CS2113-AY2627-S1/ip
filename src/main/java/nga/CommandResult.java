package nga;

/**
 * Represents the result that the command handler asks the UI to display.
 *
 * @param shouldExit whether the UI should stop accepting input
 * @param message the text that the UI should display
 */
public record CommandResult(boolean shouldExit, String message) {
}
