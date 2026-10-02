package constants;

/** Provides the FartSmella-specific messages used by {@code AbstractFella}. */
public class FartSmellaConstants extends Constants {

    /** Creates the FartSmella message and artwork set. */
    public FartSmellaConstants() {
        GREETING_STRING = FELLA_TEXT_MARKER + "bEhOLd, A fArT sMeLLA ! ! !\n" +
        FELLA_TEXT_MARKER + "aSk aND yOu sHaLL rEceIVE ! ! !\n\n";
        GOODBYE_STRING = FELLA_TEXT_MARKER + "hAHAHAHHAA, wE sHaLL mEeT aGAIN ! ! !\n\n";

        MARKED_SUCCESS_STRING = FELLA_TEXT_MARKER + "mARkEd ";
        UNMARKED_SUCCESS_STRING = FELLA_TEXT_MARKER + "uNmARkEd ";
        DELETED_SUCCESS_STRING = FELLA_TEXT_MARKER + "deLEtEd ";
        ADD_SUCCESS_STRING = FELLA_TEXT_MARKER + "aDDeD iNtO LiSt !";
        TASK_COUNT_STRING1 = FELLA_TEXT_MARKER + "nOw yOu hAvE ";
        TASK_COUNT_STRING2 =  " tAsKS iN tHe LiSt ! ! !";

        INCORRECT_COMMAND_STRING = FELLA_TEXT_MARKER + "yOUR cOmMaND iS wRoNG ! ! !\n";
        INVALID_VALUE_STRING = FELLA_TEXT_MARKER + "tHaT's nOt A vAliD vAlUe ! ! !\n";
        MISSING_TASK_NUMBER_STRING = FELLA_TEXT_MARKER + "nO tAsK nUMbER ? ? ?\n";
        INVALID_NUMBER_STRING = FELLA_TEXT_MARKER + "dO yOU kNOw wHat nUmBeRs aRe ? ? ?\n";
        ALREADY_MARKED_STRING = FELLA_TEXT_MARKER + "tHaT tAsK iS alREaDY mArKEd ! ! !\n";
        ALREADY_UNMARKED_STRING = FELLA_TEXT_MARKER + "tHaT tAsK iS alREaDY uNMarKEd ! ! !\n";
        TODO_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "tOdO nEeDs A sPaCe BeFoRe ItS dEsCrIpTiOn ! ! !\n";
        TODO_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "tOdO dEsCrIpTiOn CaNnOt Be EmPtY ! ! !\n";
        DEADLINE_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "dEaDlInE nEeDs A sPaCe BeFoRe ItS dEsCrIpTiOn ! ! !\n";
        DEADLINE_COUNT_ERROR_STRING = FELLA_TEXT_MARKER + "dEaDlInE nEeDs A dEsCrIpTiOn AnD oNe /bY dAtE ! ! !\n";
        DEADLINE_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "dEaDlInE dEsCrIpTiOn AnD dAtE cAnNoT bE eMpTy ! ! !\n";
        EVENT_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER + "eVeNt nEeDs A sPaCe BeFoRe ItS dEsCrIpTiOn ! ! !\n";
        EVENT_COUNT_ERROR_STRING = FELLA_TEXT_MARKER + "eVeNt nEeDs A dEsCrIpTiOn, OnE /fRoM, AnD oNe /tO ! ! !\n";
        EVENT_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "eVeNt dEsCrIpTiOn, StArT, AnD eNd CaNnOt Be EmPtY ! ! !\n";
        EVENT_ORDER_ERROR_STRING = FELLA_TEXT_MARKER + "eVeNt sTaRt TiMe MuSt Be EaRlIeR tHaN eNd TiMe ! ! !\n";
        DATE_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER
                + "dAtEs MuSt UsE DD-MM-YY oR DD-MM-YYYY, WiTh OpTiOnAl HHMM ! ! !\n";
        DATE_OUT_OF_RANGE_ERROR_STRING = FELLA_TEXT_MARKER
                + "dAtE oR tImE iS oUt Of RaNgE ! ! !\n";
        LIST_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER + "tHeRe ArE nO tAsKs In ThE lIsT ! ! !\n";
        LIST_FORMAT_ERROR_STRING = FELLA_TEXT_MARKER
                + "lIsT oPtIoNs MuSt UsE /sOrT nAmE/dAtE aSc/dEsC AnD /fIlTeR tOdO/dEaDlInE/eVeNt/dOnE/nOtDoNe ! ! !\n";
        LIST_NO_MATCHING_TASKS_ERROR_STRING = FELLA_TEXT_MARKER
                + "nO tAsKs MaTcH tHe SeLeCtEd FiLtEr ! ! !\n";
        FIND_EMPTY_ERROR_STRING = FELLA_TEXT_MARKER
                + "fInD nEeDs A sEaRcH tErM ! ! !\n";
        FIND_NO_MATCHING_TASKS_ERROR_STRING = FELLA_TEXT_MARKER
                + "nO tAsKs CoNtAiN tHaT sEaRcH tErM ! ! !\n";

        FELLA_STRING = "@@@@@@@@*=-=#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%%%%%%%%%##*==+#######\n"+
            "@@@@@@@@@#+::-*%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%%%%%###*+--+*#######\n"+
            "@@@@@@@@@@%*=-:-=*#%@@@@@@%%%%@@@@@@@@@@@@@@%%%%%%%%##**+=---+*#######*\n"+
            "@@@@@@@@@@@%#*==-==+**##***++++++******########**+++++==---=*########**\n"+
            "@@@@@@@@@@@@%**+=++*******++============+++++++======---:-=*########***\n"+
            "@@@@@@@@@@@@%**+**###******++++=====-====+++======-:::::-+*#########***\n"+
            "@@@@@@@@@@@@%***######**+**+++====---=======-==--:.::::-+*##########***\n"+
            "@@@@@@@@@@@@%######**##*+++===============-----:::..:.:=*#########*****\n"+
            "@@@@@@@@@@@%%##***+++*#*+========++===+*+=-::::::::..:+###########*****\n"+
            "@@@@@@@@@@%%#*+==++=+***+========++=-=+*+=-:::::.....-############*****\n"+
            "@@@@@@@@@%%%#+=:.:-=+**++===++=====-:-==--:::::.....:+###########******\n"+
            "@@@@@@@@%%%%%*==:  :+###*+=+*=-----:::.....::::.....:+##########*******\n"+
            "@@@@@@@@%%%%#=:     =%%%*++++-:::::.        .........-*########********\n"+
            "@@@@@@@@@%###+-:  .:+%%%#**+=-::::.         .........:=*######*********   ░▒▓████████▓▒░▒▓██████▓▒░░▒▓███████▓▒░▒▓████████▓▒░\n"+
            "@@@@@@@@@%####*******#%%%%#*+---:.         .:::.......-+######*********   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "@@@@@@@@@@%%%%@@@%%##%%@@%#*+---:..      ..::::..   ..:=*###******##**+   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "@@@@@@@@@@@@@@@@@@@@@@@@@%#*=---:-:::....::::.....  ..:+*#**=----+**++=   ░▒▓██████▓▒░░▒▓████████▓▒░▒▓███████▓▒░  ░▒▓█▓▒░    \n"+
            "%%%%%%%%%%@@@@@@@@@@@@@@%%*+-:---==--:::::............-*****+-::-+##*++   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "*%%%%%***#%##@@@@@@@%*++++=-::--=====-::::...........:-+++==:..           ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "++***##########%##%%*+--:::.::--=====--::::.........::-:.                 ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░    \n"+
            "=####################*=:...::--======--:::::::::::::--:..              \n"+
            "###############*####*+=:::---------===----::----:-----:..                     ░▒▓███████▓▒░▒▓██████████████▓▒░░▒▓████████▓▒░▒▓█▓▒░      ░▒▓█▓▒░       ░▒▓██████▓▒░  \n"+
            "################*##*+=----------=--==------------:::--::..                   ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "##########*#######%@#+=-::::----=====----------:::::---:.....                ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "#*####+####+####=#*@@@%*+=---======-------:::::::::::::::......               ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓██████▓▒░ ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓████████▓▒░ \n"+
            "##+###=*###-***+-**@@@@@%##***+++==--:::::......................                    ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "**=+**==+++----=++@@@@@@@@@@%#**+=--::.....................  .....                  ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ \n"+
            ":...::...=+**+++*@@@@@@@@@@@@@@%#+=-:....  .................   ........      ░▒▓███████▓▒░░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░ \n"+
            "...   ..:::-#@@@@@@@@@@@@@@@@@@@@*=:..  .....................         .\n"+
            ":  ..::::::=@@@@@@@@@@@@@@@@@@@@@#=:. ...:::::::::::..........         \n"+
            "...::::...:=@@@@@@@@@@@@@@@@@@@@%*-::::----====-==--::::.....          \n"+
            "::.......  .=@@@@@@@@@@@@@@@@%#********+++++============--::.          \n"+
            "-:::..     .=%@@@@@@@@@@@@@@@%#****+++*+++==============--::.          \n"+
            "++==+-=====--+@@@@@@@@@@@@@@@@@%#*+===++++*********+++++==-::.         \n"+
            "=-===-=--=+===*@@@@@@@@@@@%%%####*+===+*********++========-:::.        \n"+
            "=-==--======+==#@@@@@@@%#***+++***+===**####**++==========---:.        \n"+
            "+===--====--=+++#@@@@%#****++++***#*+**####**++++++++++++++==-:.       \n"+
            "+=+========--=+**#@@%#*****++++++#%%########**************+++=-:..     \n"+
            "+=++=======--==+*#%@%##****+++=++#%%%#######**********+++++++++=-:..   \n"+
            "====----====---=****###***++++==+*%%%%%%###********+++++++***#***+=:.. \n"+
            "====----=++====-=+*++####***+++++*#%%%%%%###******++++***##%%%%%%%#*=:.\n";
    }
}
