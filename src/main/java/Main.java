import java.util.Random;

import fella.AbstractFella;
import fella.FartSmella;
import fella.SmartFella;

/** Starts the SmartFella application with one of the available personalities. */
public class Main {
    /** Stores the randomly selected application personality. */
    private static AbstractFella<?> fella;

    /** Prevents construction of this entry-point utility class. */
    private Main() {
    }

    /** Creates either a {@link SmartFella} or {@link FartSmella} instance. */
    private static void instantiateFella() {
        int whichFella = new Random().nextInt(2);

        switch (whichFella) {
            case 0:
                fella = new SmartFella();
                return;
            case 1:
                fella = new FartSmella();
                return;
        }

    }
    /** Starts the selected personality and its command loop.
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        instantiateFella();

        fella.run();
    }
}
