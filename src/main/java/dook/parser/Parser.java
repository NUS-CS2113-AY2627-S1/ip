package dook.parser;

import java.util.Optional;

/**
 * Represents a parser that converts a string into an optional value.
 *
 * @param <T> The type of value produced by this parser.
 */
@FunctionalInterface
public interface Parser<T> {
    /**
     * Parses the given input into an optional value.
     *
     * @param  input The input string to parse.
     * @return       The parsed value, if successful.
     */
    Optional<T> parse(String input);
}
