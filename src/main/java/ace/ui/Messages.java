package ace.ui;

/**
 * Defines Ace's console and error messages.
 */
public final class Messages {
    public static final String BANNER =
            "\u001B[0;37m   ▄\u001B[0;91m▄▄▄▄\u001B[0;31m▄▄▄▄▄▄▄\u001B[0;37m       ▄\u001B[0;91m▄▄▄▄"
            + "\u001B[0;31m▄▄▄▄▄▄▄▄▄\u001B[0;37m    ▄\u001B[0;91m▄▄▄▄\u001B[0;31m▄▄▄▄▄▄▄▄▄\u001B[0m\n"
            + "\t\u001B[0;37m ▄\u001B[0;97m▀\u001B[0;37m            \u001B[0;31m▀▄\u001B[0;37m   ▄"
            + "\u001B[0;97m▀\u001B[0;37m        \u001B[0;31m░\u001B[0;37m \u001B[0;31m░▒▓\u001B[0;90;41m░"
            + "\u001B[0;37m  ▄\u001B[0;97m▀\u001B[0;37m        \u001B[0;31m░\u001B[0;37m \u001B[0;31m░▒▓"
            + "\u001B[0;90;41m░\u001B[0m\n"
            + "\t\u001B[0;91m▐▌\u001B[0;37m              \u001B[0;31m▐▌\u001B[0;37m \u001B[0;91m▐▌"
            + "\u001B[0;37m            \u001B[0;31m░▒\u001B[0;90;41m▒\u001B[0;37m \u001B[0;91m▐▌"
            + "\u001B[0;37m            \u001B[0;31m░▒\u001B[0;90;41m▒\u001B[0m\n"
            + "\t\u001B[0;91;41m▓\u001B[0;37m               \u001B[0;31m░█\u001B[0;37m \u001B[0;91;41m█"
            + "\u001B[0;37m              \u001B[0;31m░\u001B[0;90;41m▓\u001B[0;37m \u001B[0;91;41m█"
            + "\u001B[0;37m              \u001B[0;31m░\u001B[0;90;41m▓\u001B[0m\n"
            + "\t\u001B[0;91;41m▒\u001B[0;37m      \u001B[0;31m█▀▀\u001B[0;90m▄\u001B[0;37m      "
            + "\u001B[0;31m█\u001B[0;37m \u001B[0;91;41m▓\u001B[0;37m       \u001B[0;31m▄▄▄▄▄▄▄"
            + "\u001B[0;90m▄\u001B[0;90;41m█\u001B[0;37m \u001B[0;91;41m▓\u001B[0;37m       "
            + "\u001B[0;31m▄▄▄▄▄▄▄\u001B[0;90m▄\u001B[0;90;41m█\u001B[0m\n"
            + "\t\u001B[0;91;41m░\u001B[0;37m      \u001B[0;31m█\u001B[0;90m▄▄\u001B[0;90;41m▒"
            + "\u001B[0;37m    \u001B[0;31m┼┼█\u001B[0;37m \u001B[0;91;41m░\u001B[0;37m       "
            + "\u001B[0;31m█\u001B[0;37m     \u001B[0;31m·\u001B[0;37m   \u001B[0;91;41m░"
            + "\u001B[0;37m       \u001B[0;91m▓▄▄▄\u001B[0;90m▄\u001B[0;37m \u001B[0;31m·\u001B[0;37m  "
            + "\u001B[0m\n"
            + "\t\u001B[0;31m█┼\u001B[0;37m            \u001B[0;31m┼\u001B[0;91m┼┼\u001B[0;31m█"
            + "\u001B[0;37m \u001B[0;31m█\u001B[0;37m \u001B[0;31m┼\u001B[0;37m   \u001B[0;31m┼"
            + "\u001B[0;37m \u001B[0;31m█\u001B[0;37m       \u001B[0;31m·\u001B[0;37m \u001B[0;31m█"
            + "\u001B[0;37m \u001B[0;31m┼\u001B[0;37m     \u001B[0;90m▄▄▄▄\u001B[0;90;41m▒\u001B[0;37m   "
            + "\u001B[0;31m·\u001B[0m\n"
            + "\t\u001B[0;31m█┼┼\u001B[0;37m   \u001B[0;31m┼█▀▀█┼\u001B[0;37m   \u001B[0;31m┼"
            + "\u001B[0;91m┼\u001B[0;31m█\u001B[0;37m \u001B[0;31m█┼\u001B[0;91m┼\u001B[0;31m┼"
            + "\u001B[0;37m \u001B[0;31m┼\u001B[0;91m┼\u001B[0;31m┼█▄▄▄▄▄▄▄▄\u001B[0;37m \u001B[0;31m█┼"
            + "\u001B[0;91m┼\u001B[0;31m┼\u001B[0;37m   \u001B[0;31m┼\u001B[0;91m▓\u001B[0;31m▄▄▄▄▄▄▄▄"
            + "\u001B[0m\n"
            + "\t\u001B[0;31m█┼\u001B[0;91m┼\u001B[0;31m┼┼┼\u001B[0;91m┼\u001B[0;90;41m░\u001B[0;37m  "
            + "\u001B[0;31m█\u001B[0;91m┼\u001B[0;31m┼┼\u001B[0;37m \u001B[0;31m┼\u001B[0;91m┼"
            + "\u001B[0;90;41m░\u001B[0;37m \u001B[0;31m█┼\u001B[0;91m┼┼\u001B[0;31m┼\u001B[0;91m┼"
            + "\u001B[0;37m┼\u001B[0;91m┼\u001B[0;31m┼┼\u001B[0;91m┼┼┼┼┼┼\u001B[0;90;41m░\u001B[0;37m "
            + "\u001B[0;31m█┼\u001B[0;91m┼┼\u001B[0;37m \u001B[0;91m┼┼┼\u001B[0;31m┼┼\u001B[0;91m┼┼┼┼┼┼"
            + "\u001B[0;90;41m░\u001B[0m\n"
            + "\t\u001B[0;31m█┼\u001B[0;91m┼\u001B[0;97m┼┼\u001B[0;91m┼\u001B[0;31m┼\u001B[0;90;41m▒"
            + "\u001B[0;37m  \u001B[0;31m█\u001B[0;91m┼\u001B[0;97m┼\u001B[0;91m┼\u001B[0;31m┼"
            + "\u001B[0;91m┼\u001B[0;31m┼\u001B[0;90;41m▒\u001B[0;37m \u001B[0;31m▐▌┼\u001B[0;91m┼┼"
            + "\u001B[0;97m┼┼\u001B[0;91m┼┼┼\u001B[0;31m┼\u001B[0;91m┼\u001B[0;97m┼\u001B[0;91m┼┼"
            + "\u001B[0;31m┼\u001B[0;90;41m▒\u001B[0;37m \u001B[0;31m▐▌┼\u001B[0;91m┼┼\u001B[0;97m┼┼"
            + "\u001B[0;91m┼┼┼\u001B[0;31m┼\u001B[0;91m┼\u001B[0;97m┼\u001B[0;91m┼┼\u001B[0;31m┼"
            + "\u001B[0;90;41m▒\u001B[0m\n"
            + "\t\u001B[0;31m█┼┼\u001B[0;91m┼┼\u001B[0;31m┼┼\u001B[0;90;41m▓\u001B[0;37m  \u001B[0;31m█┼"
            + "\u001B[0;91m┼┼┼\u001B[0;31m┼┼\u001B[0;90;41m▓\u001B[0;37m \u001B[0;31m·▀▄┼┼\u001B[0;91m┼┼"
            + "\u001B[0;31m┼┼┼\u001B[0;91m┼┼┼┼\u001B[0;31m┼┼\u001B[0;90;41m▓\u001B[0;37m "
            + "\u001B[0;31m·▀▄┼┼\u001B[0;91m┼┼\u001B[0;31m┼┼┼\u001B[0;91m┼┼┼┼\u001B[0;31m┼┼"
            + "\u001B[0;90;41m▓\u001B[0m\n"
            + "\t\u001B[0;31m▀▀▀▀▀▀\u001B[0;90m▀▀\u001B[0;37m  \u001B[0;31m▀▀▀▀▀▀\u001B[0;90m▀▀"
            + "\u001B[0;37m    \u001B[0;31m▀▀▀▀▀▀▀▀▀▀▀\u001B[0;90m▀▀▀\u001B[0;37m    "
            + "\u001B[0;31m▀▀▀▀▀▀▀▀▀▀▀▀\u001B[0;90m▀▀\u001B[0m";

    public static final String HL = "______________________________";
    public static final String WELCOME_MESSAGE = "Hi, I'm Ace, hopefully I'll ace this.";
    public static final String ASSISTANCE_MESSAGE =
            "Oh, you need help? Uh... Not sure I'm the right person, but I'll try.";
    public static final String BYE_MESSAGE = "Leaving already? Sorry for the mistakes.";

    public static final String HELP_MESSAGE =
            "Here are the commands you can use:\n"
            + "\t  todo <description>\n"
            + "\t      Adds a task. Example: todo read book\n"
            + "\t  deadline <description> /by <date>\n"
            + "\t      Adds a deadline. Example: deadline submit report /by 2026-10-15\n"
            + "\t  event <description> /from <date> /to <date>\n"
            + "\t      Adds an event. Example: event trip /from 2026-10-20 /to 2026-10-22\n"
            + "\t  list\n"
            + "\t      Shows all tasks.\n"
            + "\t  mark <number>\n"
            + "\t      Marks a task as completed.\n"
            + "\t  unmark <number>\n"
            + "\t      Marks a task as not completed.\n"
            + "\t  delete <number>\n"
            + "\t      Deletes a task.\n"
            + "\t  find <keyword>\n"
            + "\t      Finds tasks containing the keyword.\n"
            + "\t  help\n"
            + "\t      Displays this help message.\n"
            + "\t  bye\n"
            + "\t      Exits Ace.\n\n"
            + "\tDates must use the yyyy-MM-dd format.\n"
            + "\tTask numbers start at 1.";

    // Error messages
    public static final String UNKNOWN_COMMAND_EXCEPTION = "I'm not sure I understand, sorry.";
    public static final String UNKNOWN_TASK_EXCEPTION = "I cannot find this task, I don't know what I did wrong.";
    public static final String OUT_OF_BOUNDS_TASK_EXCEPTION =
            "Oh, I don't know what went wrong. I swear, I'm trying, but it looks like I cannot "
            + "add any more tasks.";
    public static final String INVALID_TASK_EXCEPTION = "I don't understand... this task is... weird.";
    public static final String EMPTY_TASK_EXCEPTION =
            "I don't want to bother more, I'm just questioning the utility of an empty field.";
    public static final String NO_BY_DEADLINE_EXCEPTION =
            "I must have missed the actual deadline (given with /by). Could you retype the "
            + "command please?";
    public static final String NO_FROM_EVENT_EXCEPTION =
            "I must have missed the beginning of the event (given with /from). Could you retype "
            + "the command please?";
    public static final String NO_TO_EVENT_EXCEPTION =
            "I must have missed the end of the event (given with /to). Could you retype the "
            + "command please?";
    public static final String INVALID_DELETE_EXCEPTION = "Please... Could you specify a valid task number to delete?";
    public static final String STORAGE_EXCEPTION = "Something went wrong while accessing the saved tasks.";
    public static final String CORRUPTED_DATA_EXCEPTION = "The saved task file contains invalid data.";

    private Messages() {
    }
}
