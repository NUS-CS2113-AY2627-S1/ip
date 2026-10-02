package constants;

/** Provides shared keywords, task markers, and user-facing messages. */
public class Constants {
    /** Creates the default English message set. */
    public Constants() {
    }

    // ======================= CONSTANTS =============================
    /** Prompt printed before reading a command. */
    public String INPUT_MARKER_STRING = ">>> ";
    /** Command that ends the application. */
    public String BYE_KEYWORD = "bye";
    /** Command that displays tasks. */
    public String LIST_KEYWORD = "list";
    /** Command that marks a task. */
    public String MARK_KEYWORD = "mark";
    /** Command that unmarks a task. */
    public String UNMARK_KEYWORD = "unmark";
    /** Command that deletes a task. */
    public String DELETE_KEYWORD = "delete";
    /** Command that searches task descriptions. */
    public String FIND_KEYWORD = "find";

    /** Command that adds an undated task. */
    public String TODO_KEYWORD = "todo";
    /** Command that adds a deadline. */
    public String DEADLINE_KEYWORD = "deadline";
    /** Command that adds an event. */
    public String EVENT_KEYWORD = "event";
    /** Delimiter before a deadline date. */
    public String DEADLINE_DELIM = "/by";
    /** Delimiter before an event start. */
    public String EVENT_START_DELIM = "/from";
    /** Delimiter before an event end. */
    public String EVENT_END_DELIM = "/to";
    /** List option delimiter for sorting. */
    public String LIST_SORT_DELIM = "/sort";
    /** List option delimiter for filtering. */
    public String LIST_FILTER_DELIM = "/filter";
    /** List field name used for sorting. */
    public String LIST_NAME_FIELD = "name";
    /** List field date used for sorting. */
    public String LIST_DATE_FIELD = "date";
    /** Ascending list sort direction. */
    public String LIST_ASCENDING = "asc";
    /** Descending list sort direction. */
    public String LIST_DESCENDING = "desc";
    /** Filter for completed tasks. */
    public String LIST_DONE_FILTER = "done";
    /** Filter for incomplete tasks. */
    public String LIST_NOT_DONE_FILTER = "notdone";
    
    /** Save-file marker for a todo. */
    public String TODO_CHAR = "T";
    /** Save-file marker for a deadline. */
    public String DEADLINE_CHAR = "D";
    /** Save-file marker for an event. */
    public String EVENT_CHAR = "E";
    /** Save-file marker for a completed task. */
    public String MARKDONE_CHAR = "X";

    /** Prefix used by user-facing messages. */
    public String FELLA_TEXT_MARKER = ">> ";

    // =================== OVERRIDEABLE CONSTANTS ========================
    /** Personality artwork printed at startup. */
    public String FELLA_STRING = "";
    /** Greeting printed before the command loop. */
    public String GREETING_STRING = "<greeting message>\n";
    /** Goodbye printed after the command loop. */
    public String GOODBYE_STRING = "<goodbye message>\n";

    /** Prefix for a successful mark message. */
    public String MARKED_SUCCESS_STRING = FELLA_TEXT_MARKER + "mARKED ";
    /** Prefix for a successful unmark message. */
    public String UNMARKED_SUCCESS_STRING = FELLA_TEXT_MARKER + "uNMARKED ";
    /** Prefix for a successful delete message. */
    public String DELETED_SUCCESS_STRING = FELLA_TEXT_MARKER + "dELETED ";
    /** Message for a successful add. */
    public String ADD_SUCCESS_STRING = FELLA_TEXT_MARKER + "aDDED INTO LIST !";
    /** Prefix for the task count message. */
    public String TASK_COUNT_STRING1 = FELLA_TEXT_MARKER + "nOW YOU HAVE ";
    /** Suffix for the task count message. */
    public String TASK_COUNT_STRING2 =  " TASKS IN THE LIST ! ! !";

    /** Message for an unrecognised command. */
    public String INCORRECT_COMMAND_STRING = FELLA_TEXT_MARKER + "cOMMAND nOT rECOGNISED ! ! !\n";
    /** Message for an out-of-range task number. */
    public String INVALID_VALUE_STRING = FELLA_TEXT_MARKER + "tHAT TASK NUMBER IS NOT IN THE LIST ! ! !\n";
    /** Message for a missing task number. */
    public String MISSING_TASK_NUMBER_STRING = FELLA_TEXT_MARKER + "tELL ME WHICH TASK TO MARK OR UNMARK ! ! !\n";
    /** Message for a non-numeric task number. */
    public String INVALID_NUMBER_STRING = FELLA_TEXT_MARKER + "tHAT TASK NUMBER IS NOT A NUMBER ! ! !\n";
    /** Message for marking an already completed task. */
    public String ALREADY_MARKED_STRING = FELLA_TEXT_MARKER + "tHAT TASK IS ALREADY MARKED ! ! !\n";
    /** Message for unmarking an incomplete task. */
    public String ALREADY_UNMARKED_STRING = FELLA_TEXT_MARKER + "tHAT TASK IS ALREADY UNMARKED ! ! !\n";
    /** Message for an invalid todo command. */
    public String TODO_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "tODO NEEDS A SPACE BEFORE ITS DESCRIPTION ! ! !\n";
    /** Message for a missing todo description. */
    public String TODO_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "tODO DESCRIPTION CANNOT BE EMPTY ! ! !\n";
    /** Message for an invalid deadline command. */
    public String DEADLINE_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "dEADLINE NEEDS A SPACE BEFORE ITS DESCRIPTION ! ! !\n";
    /** Message for an incorrect deadline argument count. */
    public String DEADLINE_COUNT_ERROR_STRING = FELLA_TEXT_MARKER + "dEADLINE NEEDS A DESCRIPTION AND ONE /by DATE ! ! !\n";
    /** Message for a missing deadline description or date. */
    public String DEADLINE_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "dEADLINE DESCRIPTION AND DATE CANNOT BE EMPTY ! ! !\n";
    /** Message for an invalid event command. */
    public String EVENT_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT NEEDS A SPACE BEFORE ITS DESCRIPTION ! ! !\n";
    /** Message for an incorrect event argument count. */
    public String EVENT_COUNT_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT NEEDS A DESCRIPTION, ONE /from, AND ONE /to ! ! !\n";
    /** Message for a missing event description or date. */
    public String EVENT_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT DESCRIPTION, START, AND END CANNOT BE EMPTY ! ! !\n";
    /** Message for an event whose start is not earlier than its end. */
    public String EVENT_ORDER_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT START TIME MUST BE EARLIER THAN END TIME ! ! !\n";
    /** Message for a date that does not follow the supported format. */
    public String DATE_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER
            + "dATES MUST USE DD-MM-YY OR DD-MM-YYYY, WITH OPTIONAL HHMM ! ! !\n";
    /** Message for a date whose day, month, or time is outside its valid range. */
    public String DATE_OUT_OF_RANGE_ERROR_STRING = FELLA_TEXT_MARKER
            + "dATE OR TIME IS OUT OF RANGE ! ! !\n";
    /** Message for listing an empty task list. */
    public String LIST_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "tHERE ARE NO TASKS IN THE LIST ! ! !\n";
    /** Message for invalid list sorting or filtering options. */
    public String LIST_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER
            + "lIST OPTIONS MUST USE /sort name/date asc/desc AND /filter todo/deadline/event/done/notdone ! ! !\n";
    /** Message for a list filter with no matching tasks. */
    public String LIST_NO_MATCHING_TASKS_ERROR_STRING = FELLA_TEXT_MARKER
            + "nO TASKS MATCH THE SELECTED FILTER ! ! !\n";
    /** Message for a find command without a search term. */
    public String FIND_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER
            + "fIND NEEDS A SEARCH TERM ! ! !\n";
    /** Message for a find command with no matching tasks. */
    public String FIND_NO_MATCHING_TASKS_ERROR_STRING = FELLA_TEXT_MARKER
            + "nO TASKS CONTAIN THAT SEARCH TERM ! ! !\n";
}
