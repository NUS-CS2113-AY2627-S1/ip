package dawn.parser;

/**
 * Defines the accepted command words and the text used to describe them.
 */
public enum CommandWord {
    TODO("todo", "todo [description]", "Add a todo"),
    DEADLINE("deadline", "deadline [description] /by [due date]", "Add a deadline"),
    EVENT("event", "event [description] /from [start] /to [end]", "Add an event"),
    LIST("list", "list", "List all tasks"),
    MARK("mark", "mark [task number]", "Mark a task done"),
    UNMARK("unmark", "unmark [task number]", "Mark a task not done"),
    DELETE("delete", "delete [task number]", "Delete a task"),
    VIEW("view", "view [date]", "View tasks on a date"),
    FIND("find", "find [keyword]", "Find tasks by keyword"),
    HELP("help", "help", "Show commands"),
    BYE("bye", "bye", "Exit Dawn");

    private final String keyword;
    private final String usage;
    private final String helpDescription;

    CommandWord(String keyword, String usage, String helpDescription) {
        this.keyword = keyword;
        this.usage = usage;
        this.helpDescription = helpDescription;
    }

    /**
     * Returns the exact, case-sensitive command word entered by the user.
     */
    public String keyword() {
        return keyword;
    }

    /**
     * Returns the syntax shown in usage errors and the command guide.
     */
    public String usage() {
        return usage;
    }

    /**
     * Finds a command by its exact word, or returns null if it is unknown.
     */
    public static CommandWord fromKeyword(String keyword) {
        for (CommandWord command : values()) {
            if (command.keyword.equals(keyword)) {
                return command;
            }
        }
        return null;
    }

    /**
     * Builds a two-column command table for the introductory help text.
     */
    public static String commandGuide() {
        String commandHeading = "Command / format";
        String descriptionHeading = "Description";
        int commandWidth = commandHeading.length();
        int descriptionWidth = descriptionHeading.length();
        for (CommandWord command : values()) {
            commandWidth = Math.max(commandWidth, command.usage.length());
            descriptionWidth = Math.max(descriptionWidth, command.helpDescription.length());
        }

        String border = "+" + "-".repeat(commandWidth + 2)
                + "+" + "-".repeat(descriptionWidth + 2) + "+";
        StringBuilder guide = new StringBuilder("Here are the commands you can use:\n");
        guide.append(border).append('\n');
        appendTableRow(guide, commandHeading, descriptionHeading, commandWidth, descriptionWidth);
        guide.append(border).append('\n');
        for (CommandWord command : values()) {
            appendTableRow(guide, command.usage, command.helpDescription, commandWidth, descriptionWidth);
        }
        guide.append(border);
        return guide.toString();
    }

    /**
     * Appends a padded row while keeping both column separators aligned.
     */
    private static void appendTableRow(StringBuilder guide, String command, String description,
            int commandWidth, int descriptionWidth) {
        guide.append("| ").append(command)
                .append(" ".repeat(commandWidth - command.length()))
                .append(" | ").append(description)
                .append(" ".repeat(descriptionWidth - description.length()))
                .append(" |\n");
    }
}
