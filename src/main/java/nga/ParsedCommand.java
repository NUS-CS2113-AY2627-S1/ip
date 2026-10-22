package nga;

/**
 * Holds the command word and the text following it.
 *
 * @param keyword the lower-case command word
 * @param arguments the text after the command word
 * @param original the trimmed input line before it was split
 */
public record ParsedCommand(String keyword, String arguments, String original) {
}
