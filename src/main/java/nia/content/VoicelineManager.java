package nia.content;

/**
 * Owns Nia's personality content: the banner and her voicelines. Purely a source of
 * text - it has no knowledge of how or whether that text gets displayed, so callers
 * are responsible for printing (e.g. via Printer) whatever it returns.
 */
public class VoicelineManager {
    private static final java.util.Random RNG = new java.util.Random();

    private static final String BANNER = " _   _   ___      _    \n"
            + "| \\ | | |_ _|    / \\   \n"
            + "|  \\| |  | |    / _ \\  \n"
            + "| |\\  |  | |   / ___ \\ \n"
            + "|_| \\_| |___| /_/   \\_\\\n";

    private static final String[] STARTING_LINES = {
            "How can I help you?",
            "What do you need me to do?",
            "Mm... give me something to do, I guess.",
            "Alright, alright, I'm here. What's up?",
            "Don't mind me, just say what you need.",
            "Surprised to see me? Haha, relax. I told you we'd meet again.",
    };

    private static final String[] POST_ACTION_LINES = {
            "Anything else?",
            "I'm not much of a helper... Can I just slack off?",
            "Fine, fine... I'll help.",
            "Done. Was that so hard? ...for me, I mean.",
            "Hah, easy. What's next?",
            "Mm, noted. Don't expect this energy every time.",
            "There, sorted. Now let me go back to doing nothing.",
            "You know, most people would've just given up there.",
            "Working is so tiring, why don't you take a break?",
    };

    private static final String[] ENDING_LINES = {
            "Mm, I'm off. Try not to miss me too much.",
            "Later~ Don't work yourself too hard, okay?",
            "Guess that's enough for today. Go rest a bit."
    };

    private static String pickRandomLine(String[] possibleLines) {
        if (possibleLines.length > 0) {
            return possibleLines[RNG.nextInt(possibleLines.length)];
        }
        return null;
    }

    /** Returns Nia's ASCII-art banner. */
    public static String getBanner() {
        return BANNER;
    }

    /** Returns a random opening voiceline, for when Nia starts up. */
    public static String getStartingLine() {
        return pickRandomLine(STARTING_LINES);
    }

    /** Returns a random voiceline for after a command has been carried out. */
    static String getAfterRequestLine() {
        return pickRandomLine(POST_ACTION_LINES);
    }

    /** Returns a random farewell voiceline, for when Nia exits. */
    public static String getEndingLine() {
        return pickRandomLine(ENDING_LINES);
    }
}