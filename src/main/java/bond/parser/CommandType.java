package bond.parser;

/**
 * Identifies the operation requested by a user command.
 */
public enum CommandType {
    BYE,
    LIST,
    FIND,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    UNKNOWN
}
