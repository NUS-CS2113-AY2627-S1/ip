package constants;

/** Provides the SmartFella-specific messages used by {@code AbstractFella}. */
public final class SmartFellaConstants extends Constants {

    /** Creates the SmartFella message and artwork set. */
    public SmartFellaConstants() {
        FELLA_STRING = "                    ...                                                 \n"+
            "               .:------:                         -----:.                \n"+
            "              ---------.                         :-------.              \n"+
            "            .-------.                              :-------.            \n"+
            "           .--------                                --------            \n"+
            "          .--------.                                --------.           \n"+
            "          ---------.                               :---------.          \n"+
            "         .----------                              -----------:          \n"+
            "         :----------.                            .-----------:          \n"+
            "         :-----------            .-+++++==++****+=-----------:          \n"+
            "          -----------=+*##*++*###**++**#*+++***#*+=----------.          \n"+
            "          --------=+***##***#**#*+===+**++#*+**####*===-----.           \n"+
            "          :----==+*#####***#%#++*+=--=++++*#*++*#%%##*+===--            \n"+
            "          ---=+*#######*++*###+++=--=++++++*#*++*######**++==.          \n"+
            "          +**##%%######*+*##**+++===++++++++***++=-::-=++*****           ░▒▓███████▓▒░▒▓██████████████▓▒░ ░▒▓██████▓▒░░▒▓███████▓▒░▒▓████████▓▒░\n"+
            "         :####%##*+===+++***+++**++++++++=++**+-.    ..:--=++*=         ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "         -#####*-:.    .:=***++***++++++==+**+:.      ...-====*-        ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "         *##**+-::.      .-*##*****++=====+*+-..      ..-+****+*         ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓███████▓▒░  ░▒▓█▓▒░    \n"+
            "        :*+**##+-:.     ..:+#%###**++++====++-...   ...:=++++***=              ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "        -*#####*=...   ..::-+*****+==+++=----::.....:-======+***=              ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "        -##**++++=-:..:-======+++***%@@%+-::::::-==-----==+++**#=       ░▒▓███████▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "        =##**+++=+++++****=====+#@@@@@@@*-::::::-------=++++**##=       \n"+
            "        =###****++++===+++=====+%@@@@@@@#-::::::--======++***###=                     ░▒▓████████▓▒░▒▓████████▓▒░▒▓█▓▒░      ░▒▓█▓▒░       ░▒▓██████▓▒░  \n"+
            "        =%%##***+++++++++++++==*@@@@@@@@#=-------==+++++++*#####=                     ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "        +%####******#*****+++==*@@@@@@@@%+-::-----==++*######*#%                      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "        +%#******####**++++++**%@@@@@@@@@+-===+++++**###***####%                      ░▒▓██████▓▒░ ░▒▓██████▓▒░ ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓████████▓▒░ \n"+
            "        +@%####**#######**+++*#@@@@@@@@@@#****+===++******######.                     ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "        +@%%%###******######*##@@@@@@@@@@@*****++**+++++**######*:                    ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "         #%%%##**++++++*******#@@@@@@@@@@@#++++++++===++**#####***=                   ░▒▓█▓▒░      ░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "          %%####**+====++++++*%@@@@@@@@@@@%*+=======+++**######****#.   \n"+
            "          *@%#####*+=========*@@@@@@@@@@@@@*--===++***######**++++***   \n"+
            "          =@%%%%#####*++++==+%@@@@@@@@@@@@@#+=++**########***+====++**- \n"+
            "          %%%%%##########***#@@@@@@@@@@@@@@@%##########****++====-==++**\n"+
            "         #%%#########%%%%%##%@@@@@@@@@@@@@@@@@@%%%%#####**+++=----====++\n"+
            "        *###**###%%%%%%%%%@@@@@@@@@@@@@@@@@@@@%%%%%###%%#*++++====+==+++\n"+
            "      .####**#%%%%%####%%%@@@@@@@@@@@@@@@@@@@%%%%#########*++=++++++=+++\n"+
            "     .%%####%%%##########%%@@@@@@@@@%%%@@@@@%%%%############++===++++==+\n"+
            "     %%###%%%%#########%%%%%@@@@@@%###*#@@@%%%%%%############+=====+===-\n";

            GREETING_STRING = FELLA_TEXT_MARKER + "bEHOLD, A sMART fELLA ! ! !\n" +
                FELLA_TEXT_MARKER + "i SHALL ANSWER YOUR BURNING QUESTIONS ! ! !\n\n";
            GOODBYE_STRING = FELLA_TEXT_MARKER + "fAREWELL sTRANGER, WE SHALL MEET AGAIN ! ! !\n\n";
        
            MARKED_SUCCESS_STRING = FELLA_TEXT_MARKER + "mARKED ";
            UNMARKED_SUCCESS_STRING = FELLA_TEXT_MARKER + "uNMARKED ";
            DELETED_SUCCESS_STRING = FELLA_TEXT_MARKER + "dELETED ";
            ADD_SUCCESS_STRING = FELLA_TEXT_MARKER + "aDDED INTO LIST !";
            TASK_COUNT_STRING1 = FELLA_TEXT_MARKER + "nOW YOU HAVE ";
            TASK_COUNT_STRING2 =  " TASKS IN THE LIST ! ! !";

            INCORRECT_COMMAND_STRING = FELLA_TEXT_MARKER + "cOMMAND nOT rECOGNISED ! ! !\n";
            INVALID_VALUE_STRING = FELLA_TEXT_MARKER + "tHAT TASK NUMBER IS NOT IN THE LIST ! ! !\n";
            MISSING_TASK_NUMBER_STRING = FELLA_TEXT_MARKER + "tELL ME WHICH TASK TO MARK OR UNMARK ! ! !\n";
            INVALID_NUMBER_STRING = FELLA_TEXT_MARKER + "tHAT TASK NUMBER IS NOT A NUMBER ! ! !\n";
            ALREADY_MARKED_STRING = FELLA_TEXT_MARKER + "tHAT TASK IS ALREADY MARKED ! ! !\n";
            ALREADY_UNMARKED_STRING = FELLA_TEXT_MARKER + "tHAT TASK IS ALREADY UNMARKED ! ! !\n";
            TODO_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "tODO NEEDS A SPACE BEFORE ITS DESCRIPTION ! ! !\n";
            TODO_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "tODO DESCRIPTION CANNOT BE EMPTY ! ! !\n";
            DEADLINE_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "dEADLINE NEEDS A SPACE BEFORE ITS DESCRIPTION ! ! !\n";
            DEADLINE_COUNT_ERROR_STRING = FELLA_TEXT_MARKER + "dEADLINE NEEDS A DESCRIPTION AND ONE /by DATE ! ! !\n";
            DEADLINE_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "dEADLINE DESCRIPTION AND DATE CANNOT BE EMPTY ! ! !\n";
            EVENT_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT NEEDS A SPACE BEFORE ITS DESCRIPTION ! ! !\n";
            EVENT_COUNT_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT NEEDS A DESCRIPTION, ONE /from, AND ONE /to ! ! !\n";
            EVENT_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT DESCRIPTION, START, AND END CANNOT BE EMPTY ! ! !\n";
            EVENT_ORDER_ERROR_STRING = FELLA_TEXT_MARKER + "eVENT START TIME MUST BE EARLIER THAN END TIME ! ! !\n";
            DATE_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER
                    + "dATES MUST USE DD-MM-YY OR DD-MM-YYYY, WITH OPTIONAL HHMM ! ! !\n";
            DATE_OUT_OF_RANGE_ERROR_STRING = FELLA_TEXT_MARKER
                    + "dATE OR TIME IS OUT OF RANGE ! ! !\n";
            LIST_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "tHERE ARE NO TASKS IN THE LIST ! ! !\n";
            LIST_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER
                    + "lIST OPTIONS MUST USE /sort name/date asc/desc AND /filter todo/deadline/event/done/notdone ! ! !\n";
            LIST_NO_MATCHING_TASKS_ERROR_STRING = FELLA_TEXT_MARKER
                    + "nO TASKS MATCH THE SELECTED FILTER ! ! !\n";
            FIND_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER
                    + "fIND NEEDS A SEARCH TERM ! ! !\n";
            FIND_NO_MATCHING_TASKS_ERROR_STRING = FELLA_TEXT_MARKER
                    + "nO TASKS CONTAIN THAT SEARCH TERM ! ! !\n";
    }
}
