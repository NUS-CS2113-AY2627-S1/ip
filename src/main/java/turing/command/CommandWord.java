package turing.command;

import java.util.StringJoiner;

/**
 * The words the user can type to name a command. Collecting them here keeps
 * the vocabulary of the chatbot in one place, so the list of commands it
 * offers the user cannot fall out of step with the commands it can actually
 * build.
 */
public enum CommandWord {
    /** Adds a task with no date/time, e.g. "todo borrow book". */
    TODO("todo"),

    /** Adds a task with a due date, e.g. "deadline return book /by Sunday". */
    DEADLINE("deadline"),

    /** Adds a task with a start and an end, e.g. "event meeting /from 2pm /to 4pm". */
    EVENT("event"),

    /** Shows everything stored so far. */
    LIST("list"),

    /** Shows the tasks whose description contains some text, e.g. "find book". */
    FIND("find"),

    /** Shows the tasks falling on one day, e.g. "on 2019-10-15". */
    ON("on"),

    /** Marks a task as done, e.g. "mark 2". */
    MARK("mark"),

    /** Marks a task as not done, e.g. "unmark 2". */
    UNMARK("unmark"),

    /** Removes a task from the list, e.g. "delete 2". */
    DELETE("delete"),

    /** Ends the conversation. */
    BYE("bye");

    /** Word the user types to invoke this command. */
    private final String keyword;

    CommandWord(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the word the user types to invoke this command.
     *
     * @return Keyword, such as "delete".
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Returns the command word invoked by the given word. Keywords are matched
     * without regard to capitalization, so "BYE" and "bye" name the same command.
     *
     * @param keyword First word of the user's input.
     * @return Matching command word, or null if no command uses that word.
     */
    public static CommandWord fromKeyword(String keyword) {
        for (CommandWord commandWord : values()) {
            if (commandWord.keyword.equalsIgnoreCase(keyword)) {
                return commandWord;
            }
        }
        return null;
    }

    /**
     * Returns the keywords of every command the user can type, in the order the
     * commands are declared, e.g. "todo, deadline, event, list, mark, unmark, bye".
     *
     * @return Comma separated keywords.
     */
    public static String getKeywords() {
        StringJoiner keywords = new StringJoiner(", ");
        for (CommandWord commandWord : values()) {
            keywords.add(commandWord.keyword);
        }
        return keywords.toString();
    }
}
