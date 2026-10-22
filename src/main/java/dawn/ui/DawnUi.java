package dawn.ui;

import dawn.parser.CommandWord;

import java.util.Scanner;

/**
 * Handles user interactions, message rendering, and error formatting.
 */
public class DawnUi {
    public static final String IMG_NAME_BANNER = """
                             ..#+++.-+++##+.                    \s
                         .#...##..............+-                \s
                      .+.....##+....................            \s
                    ..................................          \s
                  -............................   ..            \s
                ..     ...................... +##########       \s
               .########. .......  ........ -#############+     \s
              ###########+  ... .##. ... . -################    \s
             ###### #######.#. ###### .##-.#######. #########   \s
            ######  . #####.+#####+###### #####. ..   #######.  \s
            #####  +###+####.#- +###+..#++##### ####   #######  \s
           -####.  +###+####  #########  #####- ####   -######  \s
           +####.   ##+ #### ########### #####.   .    .######. \s
           +####.       ####.##########..#####.        +######. \s
   ##      .####.      .####.-#######-.#.######        #######. \s
  ####      #####      #####+-#+---+###.########      ########  \s
##+#+###    ######+   ######## +###### ######################.  \s
  ##+#       ####################...#########################   \s
    #         ##############################################    \s
              .###########################################+     \s
                #########################################       \s
                 .######################-       .######         \s
                   .####-  .....####-.+++-......-.. .           \s
                      .#+........+.+#.................###.      \s
                ###.+#................................######.   \s
            +#####.++.............  ............... .##..#####. \s
          .####### #............. #- ............ .##..+####### \s
          ########+.#.......... .####+   ....   +##+ +#########.\s
         .########+   ......  .########+.#####.###+ ########### \s
          #########-.## ..-##+.#######.+######++##..##########  \s
          .######### #+-###### ####### ########.##..########.   \s
            .######+ ##.###### ######## ######.#### ######.     \s
              .####.-##+.#### ##########- .- .###### .###.      \s
              .++. #######++##########################.         \s
                   ###################################...       \s
                   -################################## .        \s
                    #################################.          \s
                     ###############################.           \s
                      #############################             \s
                      . ########################+..+.           \s
                   -###-.##.                 .+##.-###.         \s
                 .########+++                -+++#######        \s
               .-.+#######+.                  .########+#.      \s
               -#+.+###+.                       .+####+#+..     \s
                 .-                                 +##..#+     \s
            \n""";

    public static final String MAX_LINE = "____________________________________________________________\n";
    public static final String COMMAND_GUIDE = CommandWord.commandGuide();

    public static final String INTRO_MESSAGE = MAX_LINE + "\n" + IMG_NAME_BANNER
            + "Piplup here! Ready when you are!\n\n"
            + COMMAND_GUIDE + "\n"
            + MAX_LINE;
    public static final String BYE_MESSAGE =
            "Pip! No need to worry, everything is saved. See you next time!\n" + MAX_LINE;
    public static final String STORAGE_PROBLEM_BYE_MESSAGE =
            "Pip... I ran into a storage problem this session. Please check your tasks before you go. "
                    + "See you next time!\n" + MAX_LINE;

    private final Scanner scanner;

    /**
     * Constructs a user interface bound to the standard system input stream.
     */
    public DawnUi() {
        this(new Scanner(System.in));
    }

    /**
     * Constructs a user interface capturing input from a custom scanner.
     *
     * @param scanner the scanner bound to this interface.
     */
    public DawnUi(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays the application welcome message and banner.
     */
    public void showIntro() {
        System.out.println(INTRO_MESSAGE);
    }

    /**
     * Displays the command table without repeating the startup banner.
     */
    public void showCommandGuide() {
        System.out.println(COMMAND_GUIDE);
    }

    /**
     * Chooses a goodbye that reflects any storage problem during this session.
     */
    public void showBye(boolean hadStorageProblem) {
        System.out.println(hadStorageProblem ? STORAGE_PROBLEM_BYE_MESSAGE : BYE_MESSAGE);
    }

    /**
     * Displays a divider line between commands.
     */
    public void showLine() {
        System.out.println(MAX_LINE);
    }

    /**
     * Displays a response message.
     *
     * @param message the content string to echo back.
     */
    public void showMessage(String message) {
        System.out.print(message);
    }

    /**
     * Displays an error message formatting appropriately.
     *
     * @param error the specific error text.
     */
    public void showError(String error) {
        System.out.println("Pip?! " + error + "\n");
    }

    /**
     * Displays a recoverable loading warning without discarding the valid tasks.
     */
    public void showStorageWarning(String warning) {
        System.out.println("Pip?! " + warning);
    }

    /**
     * Reads the next command line, including blank input for parser validation.
     *
     * @return the raw user command line, or {@code null} when input ends.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
}
